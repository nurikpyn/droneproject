package de.reekind.droneproject.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * XML-serialisable wrapper for joda datetime values.
 */
@XmlRootElement(name = "joda_datetime")
public class XmlDateTime {

    @XmlElement(name = "millis")
    public long millis;

    public XmlDateTime() {
    }

    public XmlDateTime(long millis) {
        this.millis = millis;
    }

}
