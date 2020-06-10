
; MboxDatabaseFile

(defprotocol MboxBaseMessage
  (get-seq-num [])
  (get-raw-text [])

; {:mbox-seq-num @sequence-number :raw-mbox-text mbox-txt}
