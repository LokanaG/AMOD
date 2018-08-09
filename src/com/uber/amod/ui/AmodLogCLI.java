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

public class AmodLogCLI {

 //private static final Logger log = Logger.getLogger(Cli.class.getName());

 private String[] args = null;

 private Options options = new Options();

 public AmodLogCLI(String[] args) {

  this.args = args;
  options.addOption("h", "Help", true, "Displays this help page");
  options.addOption("host", "Host", true, "Redis host, defaults to <localhost>");
  options.addOption("port", "Port", true, "Redis port, defaults to <6379>");
  options.addOption("password", "Password", true, "Redis password, default is none");
  options.addOption("logstream", "logstream", true, "logstream file eg. </users/amod/vehicles.log>");
  options.addOption("tag", "tag", true, "Logstream tag, eg. <vehicles>");
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
   if (cmd.hasOption("host")) {
   ops.put("host", cmd.getOptionValue("host"));
    // Whatever you want to do with the setting goes here
   } 
   if (cmd.hasOption("port")) {
	   ops.put("port", cmd.getOptionValue("port"));
    // Whatever you want to do with the setting goes here
   } 
   if (cmd.hasOption("password")) {
	   ops.put("password", cmd.getOptionValue("password"));
    // Whatever you want to do with the setting goes here
   } 
   if (cmd.hasOption("logstream")) {
	   ops.put("logstream", cmd.getOptionValue("logstream"));
    // Whatever you want to do with the setting goes here
   } 
   if (cmd.hasOption("tag")) {
	   ops.put("tag", cmd.getOptionValue("tag"));
    // Whatever you want to do with the setting goes here
   }  

  } catch (ParseException e) {

   help();

  }
  System.out.println("Running exporter with Options " +ops);
  return ops;
 }

 private void help() {

  // This prints out some help
  HelpFormatter formater = new HelpFormatter();
  formater.printHelp("Main", options);
  System.exit(0);

 }

}
