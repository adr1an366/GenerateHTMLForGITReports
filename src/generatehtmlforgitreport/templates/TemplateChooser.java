/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generatehtmlforgitreport.templates;

import java.net.URL;

/**
 *
 * @author eryalus
 */
public class TemplateChooser {

    public URL getTemplateUrl() {
        return this.getClass().getResource("template.html");
    }
}
