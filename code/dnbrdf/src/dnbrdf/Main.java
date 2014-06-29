package dnbrdf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

	public static void main(String[] args) throws IOException {
		
		//Map<String, emp>empsgnb = htwk_json2hmap("http://141.57.21.45:8080/info/staff", "<gndo:preferredNameForThePerson>", "</gndo:preferredNameForThePerson>");
		
		//hmap2rdf(empsgnb, "htwkstaff.rdf");
		//dnb_gnd2rdf(empsgnb, "/home/m/Downloads/GND.rdf", "HTWK_GND.rdf");
		
		Map<String, emp>empsdnb = htwk_json2hmap("http://141.57.21.45:8080/info/staff", "<rdf:li>", "</rdf:li>");
		dnb_dnb2rdf(empsdnb, "/home/m/Downloads/DNBTitel.rdf", "HTWK_DNB.rdf");
		
		
	}
	
	/**GND.rdf contains norm data from all autors known to DNB (~150,000,000 entries, filesize ~9GB). 
	 * function collects only triples from htwkstaff (~1,400 entries)
	 * @param jsonurl
	 * @return
	 * @throws IOException
	 */
public static boolean dnb_dnb2rdf(Map<String, emp> hDnbEmps, String dnbfile, String outfile) throws IOException {
		
		//preprare filewriter
		BufferedWriter writer;
		File file = new File(outfile);
		file.createNewFile();
		writer = new BufferedWriter(new FileWriter(file));

		//// begin creating xmlrdf HEAD ////
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<rdf:RDF xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:geo=\"http://www.opengis.net/ont/geosparql#\" xmlns:rda=\"http://rdvocab.info/\" xmlns:foaf=\"http://xmlns.com/foaf/0.1/\" xmlns:sf=\"http://www.opengis.net/ont/sf#\" xmlns:isbd=\"http://iflastandards.info/ns/isbd/elements/\" xmlns:gndo=\"http://d-nb.info/standards/elementset/gnd#\" xmlns:dcterms=\"http://purl.org/dc/terms/\" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" xmlns:marcRole=\"http://id.loc.gov/vocabulary/relators/\" xmlns:lib=\"http://purl.org/library/\" xmlns:umbel=\"http://umbel.org/umbel#\" xmlns:bibo=\"http://purl.org/ontology/bibo/\" xmlns:owl=\"http://www.w3.org/2002/07/owl#\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\">\n\n");
		
		//// write BODY ////
		int hits = 0;
		String begintag = "<rdf:Description";
		String endtag = "</rdf:Description";
		String nametag = "<rdf:li>";
		StringBuilder t3 = new StringBuilder(); 	//t3 == triple for lazy people like me
		String ts3 = "";
		boolean found = false;
		
		BufferedReader br = new BufferedReader(new FileReader(dnbfile));
		String line;
		
		while ((line = br.readLine()) != null) {
			String linetrimmed = line.trim();	// performance
			if(linetrimmed.contains(nametag)) 
				System.out.println(linetrimmed);
			
			// jump in triple
			if(linetrimmed.startsWith(begintag)) 
			{			
				t3 = new StringBuilder();
				t3.append(line+"\n");
				//t3.append(line);
				//ts3=line;
				found = false;
			} else if (linetrimmed.startsWith(nametag)) {
				t3.append(line+"\n");
				//t3.append(line);
				//ts3+=line;
				if(hDnbEmps.containsKey(linetrimmed)) {
				//if(linetrimmed.contains(wanted))  {
					found = true;
					hits++;
					//System.out.println(line);
				}
			} else if (linetrimmed.startsWith(endtag)) {
				t3.append(line+"\n\n");
				//t3.append(line);
				//ts3+=line;
				if(found == true)  {
					System.out.println(hits + " " + t3.toString());
					writer.write(t3.toString());
				} else {
					//System.out.println(n);
				}
			} else {
				t3.append(line+"\n");
				//t3.append(line);
				//ts3+=line;
			}
		}
	
		
		////write FOOT////
		writer.write("</rdf:RDF>");
		
		writer.flush();
		writer.close();
		br.close();
		return true;
	
	}
	
	public static boolean dnb_gnd2rdf(Map<String, emp> hEmps, String dnbfile, String outfile) throws IOException {
	
	//preprare filewriter
	BufferedWriter writer;
	File file = new File(outfile);
	file.createNewFile();
	writer = new BufferedWriter(new FileWriter(file));

	//// begin creating xmlrdf HEAD ////
	writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	writer.write("<rdf:RDF xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:geo=\"http://www.opengis.net/ont/geosparql#\" xmlns:rda=\"http://rdvocab.info/\" xmlns:foaf=\"http://xmlns.com/foaf/0.1/\" xmlns:sf=\"http://www.opengis.net/ont/sf#\" xmlns:isbd=\"http://iflastandards.info/ns/isbd/elements/\" xmlns:gndo=\"http://d-nb.info/standards/elementset/gnd#\" xmlns:dcterms=\"http://purl.org/dc/terms/\" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" xmlns:marcRole=\"http://id.loc.gov/vocabulary/relators/\" xmlns:lib=\"http://purl.org/library/\" xmlns:umbel=\"http://umbel.org/umbel#\" xmlns:bibo=\"http://purl.org/ontology/bibo/\" xmlns:owl=\"http://www.w3.org/2002/07/owl#\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\">\n\n");
	
	
	//// write BODY ////
	int hits = 0;
	String begintag = "<rdf:Description";
	String endtag = "</rdf:Description";
	String nametag = "<gndo:preferredNameForThePerson>";
	StringBuilder t3 = new StringBuilder(); 	//t3 == triple for lazy people like me
	String ts3 = "";
	boolean found = false;
	
	BufferedReader br = new BufferedReader(new FileReader("/home/m/Downloads/GND.rdf"));
	String line;
	
	while ((line = br.readLine()) != null) {
		String linetrimmed = line.trim();	// performance
		//System.out.println(linetrimmed);
		
		// jump in triple
		if(linetrimmed.startsWith(begintag)) 
		{			
			t3 = new StringBuilder();
			t3.append(line+"\n");
			//t3.append(line);
			//ts3=line;
			found = false;
		} else if (linetrimmed.startsWith(nametag)) {
			t3.append(line+"\n");
			//t3.append(line);
			//ts3+=line;
			if(hEmps.containsKey(linetrimmed)) {
			//if(linetrimmed.contains(wanted))  {
				found = true;
				hits++;
				//System.out.println(line);
			}
		} else if (linetrimmed.startsWith(endtag)) {
			t3.append(line+"\n\n");
			//t3.append(line);
			//ts3+=line;
			if(found == true)  {
				System.out.println(hits + " " + t3.toString());
				writer.write(t3.toString());
			} else {
				//System.out.println(n);
			}
		} else {
			t3.append(line+"\n");
			//t3.append(line);
			//ts3+=line;
		}
	}

	
	////write FOOT////
	writer.write("</rdf:RDF>");
	
	writer.flush();
	writer.close();
	br.close();
	return true;

}
	
	
	public static Map<String, emp> htwk_json2hmap(String jsonurl, String starttag, String endtag) throws IOException {
		//List<emp> emps = new LinkedList<emp>();
		Map<String, emp>hEmps = new HashMap<String, emp>();
		
		JSONParser parser=new JSONParser();
		int n = 0;
		String tmp = "";
		
		URL htwkstaff = new URL(jsonurl);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(htwkstaff.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            tmp = tmp + inputLine;
        in.close();
		
        try{
	         Object obj = parser.parse(tmp);
	         JSONArray array = (JSONArray)obj;
	         ListIterator ai = array.listIterator();
	         
	         while(ai.hasNext())  {
	        	 JSONObject tmpobj = (JSONObject)ai.next();
	        	 
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
	        	 //DNB-GND.rdf name format as hasmap key value
	        	 hEmps.put(starttag+name+endtag, tmpemp);
	        	 System.out.println(starttag+name+endtag);
	        	 n++;
	         }
	      } catch (ParseException pe) {
	         System.out.println("position: " + pe.getPosition());
	         System.out.println(pe);
	      }
		System.out.println(n + " json objects parsed");
		return hEmps;
	}
	
	//public static boolean col2rdf(List<emp> empscol, String outfile) throws IOException {
	public static boolean hmap2rdf(Map<String, emp> hEmps, String outfile) throws IOException {
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
	    Iterator itr = hEmps.entrySet().iterator();
		while(itr.hasNext()) 
		{
			emp tEmp = (emp) ((Entry) itr.next()).getValue();
		
			//System.out.println("rdfwriter processing cuid" + tEmp.cuid);
			
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


