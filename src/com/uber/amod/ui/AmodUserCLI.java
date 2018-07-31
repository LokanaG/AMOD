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

public class AmodUserCLI {

 //private static final Logger log = Logger.getLogger(Cli.class.getName());

 private String[] args = null;

 private Options options = new Options();

 public AmodUserCLI(String[] args) {

  this.args = args;
  options.addOption("h", "help", false, "show help.");
  options.addOption("host", "Host", false, "Redis host, defaults to localhost");
  options.addOption("port", "Port", false, "Redis port, defaults to 6379");
  options.addOption("password", "Password", false, "Redis password, default is none");
  options.addOption("appName", "appName", true, "Source Application Name");
  options.addOption("fileName", "fileName", true, "Source File Name");
  options.addOption("correlationKey", "CorrelationKey", true, "Correlation Attribute");
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
   if (cmd.hasOption("f")) {
	   ops.put("port", cmd.getOptionValue("port"));
    // Whatever you want to do with the setting goes here
   } 
   if (cmd.hasOption("password")) {
	   ops.put("password", cmd.getOptionValue("password"));
    // Whatever you want to do with the setting goes here
   } 
   if (cmd.hasOption("appName")) {
	   ops.put("appName", cmd.getOptionValue("appName"));
    // Whatever you want to do with the setting goes here
   } 
   if (cmd.hasOption("fileName")) {
	   ops.put("fileName", cmd.getOptionValue("fileName"));
    // Whatever you want to do with the setting goes here
   } 
   if (cmd.hasOption("correlationKey")) {
	   ops.put("password", cmd.getOptionValue("correlationKey"));
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
