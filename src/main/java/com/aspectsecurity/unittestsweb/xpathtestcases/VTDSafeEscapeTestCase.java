package com.aspectsecurity.unittestsweb.xpathtestcases;

import com.aspectsecurity.unittestsweb.XPathTestCase;
import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/vtdsafeescape")
public class VTDSafeEscapeTestCase extends XPathTestCase {

    /*
     * VTD-XML: Safe when Escaping Apostrophes on XPath Expression Example
     * Proves that VTD-XML is safe from injection when using string concatenation while escaping apostrophes on
     * the XPath expression
     */
    protected void doTest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final boolean expectedSafe = true;

        try {
            // getting the file
            File temp = new File(System.getProperty("user.dir") + "/.cargo");
            String path = new File(new File(new File(new File(temp.getParent()).getParent()).getParent()).getParent()).getParent();
            path += File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
            File file = new File(path + "students.xml");
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStream.read(bytes);

            // parsing the XML
            VTDGen vtdGen = new VTDGen();
            vtdGen.setDoc(bytes);
            vtdGen.parse(true);
            VTDNav vtdNav = vtdGen.getNav();
            AutoPilot autoPilot = new AutoPilot(vtdNav);

            // querying the XML
            String query = "/Students/Student[FirstName/text()='" + request.getParameter("payload").replace("'", "&apos;") + "']"; // safe!
            autoPilot.selectXPath(query);
            ArrayList<String> resultList = new ArrayList<String>();
            int result;
            while ((result = autoPilot.evalXPath()) != -1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(vtdNav.toString(result) + " " + vtdNav.toString(result + 1));
                vtdNav.toElement(VTDNav.FIRST_CHILD);
                do {
                    int t = vtdNav.getText(); // get the index of the text (char data or CDATA)
                    if (t != -1)
                        stringBuilder.append("\n\t" + vtdNav.toNormalizedString(t));
                } while (vtdNav.toElement(VTDNav.NEXT_SIBLING));
                resultList.add(stringBuilder.toString());
                vtdNav.toElement(VTDNav.PARENT);
            }

            // testing the result
            printResults(expectedSafe, resultList, response);

        } catch (Exception ex) {
            response.getWriter().write(ex.toString());
        }
    }
}
