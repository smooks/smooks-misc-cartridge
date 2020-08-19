/*-
 * ========================LICENSE_START=================================
 * smooks-misc-cartridge
 * %%
 * Copyright (C) 2020 Smooks
 * %%
 * Licensed under the terms of the Apache License Version 2.0, or
 * the GNU Lesser General Public License version 3.0 or later.
 * 
 * SPDX-License-Identifier: Apache-2.0 OR LGPL-3.0-or-later
 * 
 * ======================================================================
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * ======================================================================
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * =========================LICENSE_END==================================
 */
package org.smooks.cartridges.cdres.trans;

import org.junit.BeforeClass;
import org.smooks.Smooks;
import org.smooks.cdr.SmooksResourceConfiguration;
import org.smooks.cdr.injector.FieldInjector;
import org.smooks.cdr.injector.Scope;
import org.smooks.cdr.lifecycle.DefaultLifecycleManager;
import org.smooks.cdr.lifecycle.LifecycleManager;
import org.smooks.cdr.lifecycle.phase.PostConstructLifecyclePhase;
import org.smooks.cdr.registry.Registry;
import org.smooks.cdr.registry.lookup.LifecycleManagerLookup;
import org.smooks.container.ApplicationContext;
import org.smooks.container.MockApplicationContext;
import org.smooks.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.junit.Test;

import static org.junit.Assert.*;

public class BaseTransUnitsTest {

    private static LifecycleManager lifecycleManager;
    private static Registry registry;

    @BeforeClass
    public static void beforeClass() {
        ApplicationContext applicationContext = new MockApplicationContext();
        registry = applicationContext.getRegistry();
        lifecycleManager = registry.lookup(new LifecycleManagerLookup());
    }
    
    @Test
    public void test_RenameAttributeTU() {
        RenameAttributeTU tu = new RenameAttributeTU();

        SmooksResourceConfiguration resourceConfiguration = new SmooksResourceConfiguration("body", "device", "xxx");
        resourceConfiguration.setParameter("attributeName", "attrib1");
        resourceConfiguration.setParameter("attributeNewName", "attrib2");

        lifecycleManager.applyPhase(tu, new PostConstructLifecyclePhase(new Scope(registry, resourceConfiguration, tu)));

        Document doc = parseCPResource("/testpage1.html");
        Element body = (Element) XmlUtil.getNode(doc, "/html/body");
        tu.visitAfter(body, null);
        assertEquals("Default overwrite protection failed.", "value2", body.getAttribute("attrib2"));

        resourceConfiguration.setParameter("overwrite", "true");
        lifecycleManager.applyPhase(tu, new PostConstructLifecyclePhase(new Scope(registry, resourceConfiguration, tu)));
        tu.visitAfter(body, null);
        assertFalse("Rename failed to remove target attribute.", body.hasAttribute("attrib1"));
        assertEquals("Overwrite failed.", "value1", body.getAttribute("attrib2"));
    }

    @Test
    public void test_RemoveAttributeTU() {
        SmooksResourceConfiguration resourceConfiguration = new SmooksResourceConfiguration("body", "device", "xxx");
        resourceConfiguration.setParameter("attributeName", "attrib1");

        RemoveAttributeTU tu = new RemoveAttributeTU();
        lifecycleManager.applyPhase(tu, new PostConstructLifecyclePhase(new Scope(registry, resourceConfiguration, tu)));

        Document doc = parseCPResource("/testpage1.html");
        Element body = (Element) XmlUtil.getNode(doc, "/html/body");
        assertTrue("XPath failed - test corrupted.", body.hasAttribute("attrib1"));
        tu.visitAfter(body, null);
        assertFalse("Failed to remove target attribute.", body.hasAttribute("attrib1"));
    }

    @Test
    public void test_RenameElementTU() {
        SmooksResourceConfiguration resourceConfiguration = new SmooksResourceConfiguration("body", "device", "xxx");
        resourceConfiguration.setParameter("replacementElement", "head");

        RenameElementTU tu = new RenameElementTU();
        lifecycleManager.applyPhase(tu, new PostConstructLifecyclePhase(new Scope(registry, resourceConfiguration, tu)));

        Document doc = parseCPResource("/testpage1.html");
        Element body = (Element) XmlUtil.getNode(doc, "/html/body");
        tu.visitAfter(body, null);
        assertNull("Failed to rename target element.", XmlUtil.getNode(doc, "/html/body"));
        assertNotNull("Failed to rename target element.", XmlUtil.getNode(doc, "/html/head"));
    }

    @Test
    public void test_RenameElementTU_root_element() {
        Document doc = parseCPResource("/testpage1.html");
        SmooksResourceConfiguration resourceConfiguration = new SmooksResourceConfiguration("body", "device", "xxx");
        Element body = (Element) XmlUtil.getNode(doc, "/html/body");
        RenameElementTU tu;

        resourceConfiguration.setParameter("replacementElement", "head");
        tu = new RenameElementTU();
        lifecycleManager.applyPhase(tu, new PostConstructLifecyclePhase(new Scope(registry, resourceConfiguration, tu)));
        
        tu.visitAfter(body, null);
        assertNull("Failed to rename target element.", XmlUtil.getNode(doc, "/html/body"));
        assertNotNull("Failed to rename target element.", XmlUtil.getNode(doc, "/html/head"));
    }

    @Test
    public void test_RemoveElementTU() {
        Document doc = parseCPResource("/testpage1.html");
        SmooksResourceConfiguration resourceConfiguration = new SmooksResourceConfiguration("body", "device", "xxx");
        Element body = (Element) XmlUtil.getNode(doc, "/html/body");
        RemoveElementTU tu = new RemoveElementTU();
        lifecycleManager.applyPhase(tu, new PostConstructLifecyclePhase(new Scope(registry, resourceConfiguration, tu)));
        
        tu.visitAfter(body, null);
        assertNull("Failed to remove target element.", XmlUtil.getNode(doc, "/html/body"));
    }

    @Test
    public void test_RemoveElementTU_root_element() {
        Document doc = parseCPResource("/testpage1.html");
        SmooksResourceConfiguration resourceConfiguration = new SmooksResourceConfiguration("html", "xxx");
        Element body = (Element) XmlUtil.getNode(doc, "/html/body");
        RemoveElementTU tu = new RemoveElementTU();
        lifecycleManager.applyPhase(tu, new PostConstructLifecyclePhase(new Scope(registry, resourceConfiguration, tu)));
        
        // So remove the root element...
        tu.visitAfter(doc.getDocumentElement(), null);
        assertEquals("Failed to remove root element.", body, doc.getDocumentElement());

        // Try removing the new root element - should fail because the body element has no child elements...
        tu.visitAfter(doc.getDocumentElement(), null);
        assertEquals("Remove root element but shouldn't have.", body, doc.getDocumentElement());
    }

    @Test
    public void test_SetAttributeTU() {
        Document doc = parseCPResource("/testpage1.html");
        SmooksResourceConfiguration resourceConfiguration = new SmooksResourceConfiguration("body", "device", "xxx");
        Element body = (Element) XmlUtil.getNode(doc, "/html/body");
        SetAttributeTU tu = new SetAttributeTU();

        resourceConfiguration.setParameter("attributeName", "attrib1");
        resourceConfiguration.setParameter("attributeValue", "value3");
        lifecycleManager.applyPhase(tu, new PostConstructLifecyclePhase(new Scope(registry, resourceConfiguration, tu)));
        tu.visitAfter(body, null);
        assertEquals("Default overwrite protection failed.", "value1", body.getAttribute("attrib1"));

        resourceConfiguration.setParameter("overwrite", "true");
        lifecycleManager.applyPhase(tu, new PostConstructLifecyclePhase(new Scope(registry, resourceConfiguration, tu)));
        tu.visitAfter(body, null);
        assertEquals("Overwrite failed.", "value3", body.getAttribute("attrib1"));
    }


    public Document parseCPResource(String classpath) {
        try {
            return XmlUtil.parseStream(getClass().getResourceAsStream(classpath), XmlUtil.VALIDATION_TYPE.NONE, true);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }
}
