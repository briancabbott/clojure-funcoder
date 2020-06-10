
package clojarsAnalizer.core.model.packageDescriptor.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import clojarsAnalizer.core.model.packageDescriptor.maven.Model;


public class MVNReader {

    public MVNReader() {}
    public MVNReader(String mavenPackage) {

    }
    public MVNReader(File mavenPackage) {}


    public Model unmarJaxbProjectFile(String mavenPomFilePath) {
      try {
          // create a JAXBContext capable of handling classes generated into
          // the primer.po package
          JAXBContext jc = JAXBContext.newInstance("clojarsAnalizer.core.model.packageDescriptor.maven");

          // create an Unmarshaller
          Unmarshaller u = jc.createUnmarshaller();

          // unmarshal a po instance document into a tree of Java content
          // objects composed of classes from the primer.po package.
          JAXBElement<?> pomElement = (JAXBElement<?>)u.unmarshal( new FileInputStream(mavenPomFilePath) );
          Model pomModel = (Model)pomElement.getValue();

          return pomModel;
      } catch( JAXBException je ) {
          je.printStackTrace();
      } catch( IOException ioe ) {
          ioe.printStackTrace();
      }
      return null;
    }

    public static void main(String args[])
    {
        Model myModel = new MVNReader().unmarJaxbProjectFile("/Users/brian/pom-files-1591592584924/_A/analytics-1.0.1.pom");
    }
}
