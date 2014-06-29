package semanticweb;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.net.*;
import java.io.*;
import java.util.*;

public class Main {
	
	public static void main(String[] args) throws IOException, ParseException {
		
		col2rdf(json2col("http://141.57.21.45:8080/info/staff"), "employees.rdfxml");
	
	}

	public static List<emp> json2col(String jsonurl) throws IOException {
		List<emp> emps = new LinkedList<emp>();
		JSONParser parser=new JSONParser();
		
		String tmp = "";
		
		URL htwkstaff = new URL(jsonurl);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(htwkstaff.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            tmp = tmp + inputLine;
        	System.out.print(inputLine);        
        in.close();
		
        try{
	         Object obj = parser.parse(tmp);
	         JSONArray array = (JSONArray)obj;
	         ListIterator ai = array.listIterator();
	         
	         while(ai.hasNext())  {
	        	 //System.out.println(ai.next());
	        	 //Object oo = ai.next();
	        	 JSONObject tmpobj = (JSONObject)ai.next();
	        	 //if(DEBUG) System.out.println(n++ + " cuid: " +  tmpobj.get("cuid") +" name: " + tmpobj.get("name") + " degree: " +  tmpobj.get("degree") + " faculty: " +  tmpobj.get("faculty"));

	        	 String name = (String)tmpobj.get("name");
	        	 String lastname = name.split("\\,")[0];
	        	 String firstname = name.split("\\,")[1];
	        	 
	        	 emp tmpemp = new emp(	Integer.parseInt((String)tmpobj.get("cuid")),
	        			 				lastname,
	        			 				firstname,
	        			 				(String)tmpobj.get("degree"),
	        			 				(String)tmpobj.get("faculty"),
	        			 				0,
	        			 				0);
	         
	        	 emps.add(tmpemp);
	         
	         }
	      } catch (ParseException pe) {
	         System.out.println("position: " + pe.getPosition());
	         System.out.println(pe);
	      }
		
		return emps;
	}
	
	
	public static boolean col2rdf(List<emp> empscol, String outfile) throws IOException {
		//preprare filewriter
		BufferedWriter writer;
		File file = new File(outfile);
	    file.createNewFile();
	    writer = new BufferedWriter(new FileWriter(file));

 	    //begin creating xmlrdf HEAD
	    writer.write("<rdf:RDF\n");
	    writer.write("  xmlns:semweb=\"https://raw.githubusercontent.com/mkirbst/semanticweb/master/scheme#\"\n");
	    writer.write("  xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n");
	    writer.write(">\n\n");
	    
	    //BODY - employees
	    int n = 0;
	    Iterator itr = empscol.listIterator();
		while(itr.hasNext()) 
		{
			emp tEmp = (emp)itr.next();
			System.out.println("rdfwriter processing cuid" + tEmp.cuid);
			
			writer.write("  <semweb:Employee rdf:about=\"https://raw.githubusercontent.com/mkirbst/semanticweb/master/htwkstaff.json#cuid" + tEmp.cuid + "\">\n");
			writer.write("    <semweb:cuid>"+tEmp.cuid+"</semweb:cuid>\n");
			writer.write("    <semweb:lastname>"+tEmp.lastname+"</semweb:lastname>\n");
			writer.write("    <semweb:firstname>"+tEmp.firstname+"</semweb:firstname>\n");
			writer.write("    <semweb:degree>"+tEmp.degree+"</semweb:degree>\n");
			writer.write("    <semweb:faculty>"+tEmp.faculty+"</semweb:faculty>\n");
			writer.write("    <semweb:dnbautorid>"+tEmp.dnbautorid+"</semweb:dnbautorid>\n");
			writer.write("    <semweb:birth>"+tEmp.birth+"</semweb:birth>\n");
			writer.write("  </semweb:Employee>\n");
			n++;
		}
		
	    //FOOT
	    writer.write("</rdf:RDF>\n");
	    writer.flush();
	    writer.close();
		System.out.println("closing file " + outfile + " (containing "+n+" triples) ...");
	    return true;
	}

}

