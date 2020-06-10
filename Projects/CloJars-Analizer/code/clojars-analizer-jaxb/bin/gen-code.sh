

JAXB_LOCATION=/Users/brian/dev_space/GitHub-briancabbott/clojure-funcoder/Projects/CloJars-Analizer/resources/code/JAXB/jaxb-ri-3.0.0-M4/jaxb-ri
PATH=$PATH:JAXB_LOCATION/bin

OUT_DIR=./src
TARGET_PACKAGE=clojarsAnalizer.core.model.packageDescriptor.maven
XML_SCHEMA_FILE=maven-4.0.0.xsd


$JAXB_LOCATION/bin/xjc.sh -d $OUT_DIR -p $TARGET_PACKAGE -xmlschema $XML_SCHEMA_FILE
