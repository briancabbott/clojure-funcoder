(ns mbox-extractor.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [rx.lang.clojure.interop :as rx])
  (:import rx.Observable rx.subscriptions.Subscriptions java.io.FileReader java.io.BufferedReader)
  ; (:import []
  ;          [rx.Observable]
  ;          [rx.subscriptions.Subscriptions])
  ; (:gen-class)
  )

(def MAX-FILE-BUFFER-SIZE (* 10 1024 1024))

(def sequence-number (atom 0))
(def line-counter (atom 0))

(defn inc-sequence-number []
  (swap! sequence-number inc))

(defn inc-line-counter []
  (swap! line-counter inc))

(defn read-line-from-buffered-reader [buffered-reader]
  (let [line (.readLine buffered-reader)]
    (if (not (nil? line))
      (inc-line-counter))
    line))

(defn is-first-message-header? []
  (not (>= @line-counter 1)))

(defn is-mbox-starting-line? [prev-line line]
  (let [from-header-regex #"^From\s\w+@\w+ (Sun|Mon|Tue|Wed|Thu|Fri|Sat) (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \d+ \d+:\d+:\d+ \p{Punct}\d+ \d+$"
        ;from-header-regex (re-pattern "^From\\s\\w+@\\w+\\s(Mon|Tue|Wed|Thu|Fri|Sat|Sun)\\s(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s\\d+\\s\\d{2}:\\d{2}:\\d{2}\\s+\\d+\\s\\d+$")
        find-results (re-find from-header-regex line)
        line-matches (if (instance? String find-results)
                       (not (nil? (re-find from-header-regex line)))
                       (and
                         (not (nil? find-results))
                         (> (count find-results) 0)))
        prev-matches (str/blank? prev-line)]
    (if (is-first-message-header?)
      line-matches
      (and prev-matches line-matches))))

(defn read-mbox-message-from [line br]
  (let [msg-lines (atom [])]
    (swap! msg-lines conj line)
    (loop [prv-line ""
           nxt-line (read-line-from-buffered-reader br)]
      (if (and
            (string? nxt-line)
            (not (is-mbox-starting-line? prv-line nxt-line)))
        (do
          (swap! msg-lines conj nxt-line)
          (recur (last @msg-lines)
                  (read-line-from-buffered-reader br)))
        (do (prn "not a string:")
            (prn (.getClass nxt-line)))))
    (inc-sequence-number)
    (str/join "\n" @msg-lines)))

(defn extractor-via-observable [mbox-path]
  (Observable/create
    (rx/action [^rx.Subscriber s]
      (let [fr (FileReader. mbox-path)
            br (BufferedReader. fr)
            f (future
                (try
                  (loop [prev-line ""
                         line (read-line-from-buffered-reader br)]
                    (if (not (nil? line))
                      (if (is-mbox-starting-line? prev-line line)
                        (let [mbox-msg (read-mbox-message-from line br)]
                          ;; here - do our observable action...
                          (-> s (.onNext {:seq-num @sequence-number :raw-text mbox-msg}))))
                      (-> s .onCompleted))
                    (recur line
                           (read-line-from-buffered-reader br)))
                  (catch Exception e (-> s (.onError e)))))]
        ; a subscription that cancels the future if unsubscribed
        (.add s (Subscriptions/create (rx/action [] (future-cancel f))))
        ))))

(defn parse-large-mbox-file [mbox-path]
  (let [fr (FileReader. mbox-path)
        br (BufferedReader. fr)]
    (loop [prev-line ""
           line (read-line-from-buffered-reader br)]
      (if (not (nil? line))
        (if (is-mbox-starting-line? prev-line line)
          (let [mbox-msg (read-mbox-message-from line br)]
            ; (println "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
            ; (println "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
            ; (println "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
            ; (println "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
            ; (prn "Mbox message is: ")
            ; (println mbox-msg)
            )))
        (recur line
               (read-line-from-buffered-reader br)))))

(defn parse-small-mbox-file [mbox-path])
(defn read-mbox-file [mbox-path]
  (let [file-inf (io/file mbox-path)
        file-len (.length file-inf)]
    (if (> file-len MAX-FILE-BUFFER-SIZE)
      (parse-large-mbox-file mbox-path)
      (parse-small-mbox-file mbox-path))
    (println file-len)))

    ; From 1661828917674532318@xxx Sun Mar 22 02:19:17 +0000 2020
    ; X-GM-THRID: 1661828917674532318
    ; X-Gmail-Labels: Inbox,Category Promotions,Unread
    ; Delivered-To: briancabbott@gmail.com
    ; Received: by 2002:a0c:b985:0:0:0:0:0 with SMTP id v5csp2590545qvf;
    ;         Sat, 21 Mar 2020 19:19:17 -0700 (PDT)
    ; X-Google-Smtp-Source: ADFU+vtxv/+BZDzf3m1kRQPscGcj7zQHoaokkvvc+i5sFMZTbQ5GqRbnD3muvwIpFpwNyAacrQVH
    ; X-Received: by 2002:a17:90a:cc0e:: with SMTP id b14mr18429482pju.75.1584843556948;
    ;         Sat, 21 Mar 2020 19:19:16 -0700 (PDT)
    ; ARC-Seal: i=1; a=rsa-sha256; t=1584843556; cv=none;
    ;         d=google.com; s=arc-20160816;
    ;         b=N5B6RislcGrymru6bth8QlLg63wplMaH2px7gQoesiZ1nmqaTH4gwhAoHzKypAk3kf
    ;          luYFVrPo/G2qc/LdJJqsiKqPY0Xju0g5quImqt9NpFSLGd2NDLWz6DWcn7v71YPh3CE9
    ;          hTSbU20pTQu2cUqqmEVkEC3/7Y+EvDmBvIqV9n3rWQcbNo/rGayuzAADHoJJPDs8bUuN
    ;          cGvolqfFM1f3LhSHgX6h7e5tFj1QdDg6zXbCnoX4hAfdHuZ2CPQvu/VynnDmfZwX45F4
    ;          Hd1ZLKcniQNvgyk/ESVDYPHB/gROJ3KXVLjLAyq9DgVVBACvt0VaPgHZ9FKQRcgzGaBx
    ;          tAyw==
    ; ARC-Message-Signature: i=1; a=rsa-sha256; c=relaxed/relaxed; d=google.com; s=arc-20160816;
    ;         h=list-id:list-unsubscribe:content-transfer-encoding:mime-version
    ;          :subject:message-id:to:from:date:dkim-signature;
    ;         bh=gOsnucVYFV8TxUXP51ulBhZ4EUJ0bdU+Fjf9Ya/s2GM=;
    ;         b=AZ5M46x7V0zO9ziV9SXEFmiVgSKhYaOjr1OwA0GoIrfleGWR7/iRWdu6I1Pd8lw7Fu
    ;          JfGiJFs9djf4D7ui+lbclPJRdbGPXCGFghF1CF3OYCoQ8P1XrxnCWpOdjGwai48L9qz9
    ;          ptyGL5nVGRcUzM6p4ipmA8DtbQwi3uYZORuEH3cYTXGbypPn7VsWoMkN/e1ELGM+0GTN
    ;          SEFZ7/fOJQ49miP+59goIC7BnROD/MDXaOE6E8VSXjNlQcMrTdNZy6yihxq+LbnlpJtp
    ;          8S8QiQ+JAqD6w+9MKGp4FUG1cijOwVgURNVrovlsHfJm3CHdawBXFULX8IcXOaliJnhr
    ;          y3FA==
    ; ARC-Authentication-Results: i=1; mx.google.com;
    ;        dkim=pass header.i=@r.groupon.com header.s=s2048d20190430 header.b=WYu6eWf+;
    ;        spf=pass (google.com: domain of b52a0f28-278d-11e2-b88f-00259069d5fe@bounce.r.groupon.com designates 50.115.211.143 as permitted sender) smtp.mailfrom=b52a0f28-278d-11e2-b88f-00259069d5fe@bounce.r.groupon.com;
    ;        dmarc=pass (p=REJECT sp=REJECT dis=NONE) header.from=r.groupon.com
    ; Return-Path: <b52a0f28-278d-11e2-b88f-00259069d5fe@bounce.r.groupon.com>
    ; Received: from mta143s1.r.groupon.com (mta143s1.r.groupon.com. [50.115.211.143])
    ;         by mx.google.com with ESMTPS id 18si8023805pll.246.2020.03.21.19.19.16
    ;         for <briancabbott@gmail.com>
    ;         (version=TLS1_2 cipher=ECDHE-RSA-AES128-GCM-SHA256 bits=128/128);
    ;         Sat, 21 Mar 2020 19:19:16 -0700 (PDT)
    ; Received-SPF: pass (google.com: domain of b52a0f28-278d-11e2-b88f-00259069d5fe@bounce.r.groupon.com designates 50.115.211.143 as permitted sender) client-ip=50.115.211.143;
    ; Authentication-Results: mx.google.com;
    ;        dkim=pass header.i=@r.groupon.com header.s=s2048d20190430 header.b=WYu6eWf+;
    ;        spf=pass (google.com: domain of b52a0f28-278d-11e2-b88f-00259069d5fe@bounce.r.groupon.com designates 50.115.211.143 as permitted sender) smtp.mailfrom=b52a0f28-278d-11e2-b88f-00259069d5fe@bounce.r.groupon.com;
    ;        dmarc=pass (p=REJECT sp=REJECT dis=NONE) header.from=r.groupon.com
    ; DKIM-Signature: v=1; a=rsa-sha256; c=relaxed/relaxed; d=r.groupon.com;
    ;         s=s2048d20190430; t=1584843556;
    ;         bh=gOsnucVYFV8TxUXP51ulBhZ4EUJ0bdU+Fjf9Ya/s2GM=;
    ;         h=Date:From:To:Message-ID:Subject:Content-Type;
    ;         b=WYu6eWf+mfor44J+u+1Up3w2Rnr7bLk+7yn2yqnQX09ijqazrRyBcs8U+j1kKH2y3
    ;          5KXuN8ds0KXpd1mGMGHA7cHZ2L6rq43/p2As5FQwQhHRr8wybgzspsdFqI3kOvCvuB
    ;          AbY4GeXd0KlnyvrBahnnWieKn+yCLweJ5jn0cZen5dNgBwwbepVpCn6IqzbnK213hg
    ;          pb1v7t8yzs49nCpSe4IkVeu91F3kQaLPP3Dnu84dq1VwjUJLLYbS06lkEWnrShcpNP
    ;          06VnTY3OaHw+aJMHG65uWDVmJ20/dxDYKsqsre+gFaSlrCMHgShOK8L7zNc2EiP+j0
    ;          5U+Jk3laB2gMA==



; (defn -main
;   "I don't do a whole lot ... yet."
;   [& args]
;   (println (str "Max file buffer size: " MAX-FILE-BUFFER-SIZE))
;   (read-mbox-file "/Volumes/data-store/takeout/Takeout/Mail/All mail Including Spam and Trash.mbox")
;   (println "Hello, World!"))
