package com.uber.amod.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class AmodCLI {

 //private static final Logger log = Logger.getLogger(Cli.class.getName());

 private String[] args = null;

 private Options options = new Options();

 public AmodCLI(String[] args) {

  this.args = args;
  options.addOption("h", "help", false, "show help.");
  options.addOption("i", "inputLog", true, "Logstash input for processing");
  options.addOption("f", "statsOutput", true, "Stats output file");

 }

 public Map<String,String> parse() {

  Map<String,String> ops = new HashMap<String,String>();
  CommandLineParser parser = new BasicParser();
  CommandLine cmd = null;

  try {

   cmd = parser.parse(options, args);

   if (cmd.hasOption("h"))
   {
    help();
   }
   if (cmd.hasOption("i")) {
   ops.put("i", cmd.getOptionValue("i"));
    // Whatever you want to do with the setting goes here
   } 
   if (cmd.hasOption("f")) {
	   ops.put("f", cmd.getOptionValue("f"));
    // Whatever you want to do with the setting goes here
   } 
   
   else {
    help();
   }

  } catch (ParseException e) {

   help();

  }
return ops;
 }

 private void help() {

  // This prints out some help
  HelpFormatter formater = new HelpFormatter();
  formater.printHelp("Main", options);
  System.exit(0);

 }

}
