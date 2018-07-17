# AMOD
Authorization Modeler - ABAC Simulation
The scope of the Permissions project is strictly on internal tools. Incremental improvements in the authorization logic for internal tools at Uber will help achieve the visibility of  “who has access to what”.

The approach of the project is to define and refine a list of user attributes that will result in consistent as accurate authorization access to an application. These attributes can be used to create groups that make managing the provisioning of the access request simpler. A constraint of the project is not to impact changes to the application with the new list of attributes. 

Throughout the Permissions project the existing authentication flow will remain unaltered, the only alteration is to update the mapping in Pullo as each application is onboarded into the dynamic model.  For additional information on the details of the existing functionality and longer term plans for uSSO migration can be found here (uSSO RFC).

Permission Analysis Process
The project is going to use a strategy that first defines a list of attributes that make sense for a given user group to access an application.  The list of attributes is constructed from data sources such as the AD, Workday, and FlexForce systems that contain user attributes. With this approach, authorization to applications will be driven by individual user attributes.  When interpreted by the policy engine, these attributes act as permissions for applications and services within Uber.

To improve efficiency and address one of the key pain points of the existing environment, the team has developed an authorization modeling framework.  This tool (Amod - Authorization Modeler) allows the operations staff to simulate access patterns and answer “what-if” scenarios when considering a change to a authorization policy in Pullo.


The diagram below, outlines the high-level process for running simulations and validation of the candidate attribute model(s) which will be developed as a repeatable process model for the permissions project.
As part of the effort to validate models for the Permissions V0.5 project, the team has settled on a framework to test attribute models.  The intention of the framework is to allow many multiple scenario comparisons to occur in a automated fashion.

Three key platforms are involved in model scoring, these are as follows:
Analytics Platform - Built on R-Studio and MySQL
Authorization Modeler Platform (Amod) - Custom code framework built on Go, Lambda & DynamoDB
ELK ML Platform - ElasticSearch, Logstash, Kibana and the ML(Machine Learning) X-pack 

Each of these contribute to the ultimate technical goal, which is to produce dynamic group models that achieve automated assignment for users.  Thereby reducing the need to manually assign and request group access.  This benefits application owners and end-users alike.
