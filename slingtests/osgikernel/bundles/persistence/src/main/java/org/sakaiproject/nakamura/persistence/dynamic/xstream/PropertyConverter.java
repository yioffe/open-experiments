/*
 * Licensed to the Sakai Foundation (SF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The SF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.sakaiproject.nakamura.persistence.dynamic.xstream;

import com.google.common.collect.ImmutableSet;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.Set;

public class PropertyConverter implements Converter {

  private static final Set<String> filter = ImmutableSet.of("eclipselink.jdbc.url","eclipselink.jdbc.driver",
      "eclipselink.jdbc.user", "eclipselink.jdbc.password", "eclipselink.target-server",
      "eclipselink.jdbc.write-connections.min", "eclipselink.jdbc.read-connections.min",
      "eclipselink.logging.level", "eclipselink.logging.timestamp",
      "eclipselink.logging.session", "eclipselink.logging.thread",
      "eclipselink.logging.exceptions", "eclipselink.ddl-generation.output-mode",
      "eclipselink.ddl-generation");

  public void marshal(Object value, HierarchicalStreamWriter writer,
      MarshallingContext context) {
    Property property = (Property) value;
    if (!filter.contains(property.getName())) {
      writer.addAttribute("name", property.getName());
      writer.addAttribute("value", property.getValue());
    } else {
      // if this isnt done, then EclipseLink wont load the properties. Nasty Hack!
      writer.addAttribute("name", "filtered-"+property.getName());
      writer.addAttribute("value", property.getValue());
    }
  }

  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    Property property = new Property();
    property.setName(reader.getAttribute("name"));
    property.setValue(reader.getAttribute("value"));
    return property;
  }

  @SuppressWarnings("unchecked")
  public boolean canConvert(Class clazz) {
    return clazz.equals(Property.class);
  }

}
