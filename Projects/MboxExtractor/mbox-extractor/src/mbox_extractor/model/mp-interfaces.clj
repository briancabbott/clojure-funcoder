(defprotocol MPEmailAttribute
  (get-seq-num [])
  (get-label [])
  (get-value []))

(defprotocol MPEmailUnstructuredContent
  (get-mime-type [])
  (get-raw-content [])
  (get-references []))


(defprotocol MPEmailMessage
  (get-header-attributes
    "List of name-value-pairs, in order of appeared sequence in message"
    [])

  (get-unstructured-content
    "An unstructured/unparsed/un-nlu'd model of the content... (Size, mime-type,
     raw content, perhaps anything that might be downloaded from links to aid
     in the NLU/visualization/presentation/or further analysis sequences)"
    [])

  (get-resolved-references
    ""
    [])

  (get-parser-configuration
    "The configuration of the message-parser that drove the parse-operation on this message."
    [])
)

(defprotocol MPMessageParserConfiguration
  (get-resolve-references
    "Weather or not the referenced objects should be downloaded and attached to the parsed message."
    [])
