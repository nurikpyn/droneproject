package de.reekind.droneproject.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Convert between joda datetime and XML-serialisable millis represented as long
 */
public class XmlDateTimeAdapter  extends XmlAdapter<XmlDateTime, DateTime> {

    @Override
    public XmlDateTime marshal(DateTime v) throws Exception {

        if(v != null)
            return new XmlDateTime(v.getMillis());
        else
            return new XmlDateTime(0);
    }

    @Override
    public DateTime unmarshal(XmlDateTime v) throws Exception {
        return new DateTime(v.millis, DateTimeZone.UTC);
    }
}
