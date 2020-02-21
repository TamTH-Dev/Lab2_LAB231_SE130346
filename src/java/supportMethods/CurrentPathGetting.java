/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supportMethods;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 *
 * @author hoang
 */
public class CurrentPathGetting {

    public String getPath() throws UnsupportedEncodingException {
        String path = this.getClass().getClassLoader().getResource("").getPath();
        String fullPath = URLDecoder.decode(path, "UTF-8");
        String splittedPath[] = fullPath.split("/build/web/WEB-INF/classes/");
        fullPath = splittedPath[0];
        String currentPath = new File(fullPath).getPath(); 
        return currentPath;
    }
}
