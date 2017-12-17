# Sleuth valve tomcat
This is a demo project that contains 3 services, using spring boot and Sleuth. The goal is to add the traceID on the access log of the edge server by adding a Tomcat Valve that injects Sleuth headers if missing

The blog post that explains this approach: https://aboullaite.me/configuring-a-sleuth-valve-springboot/
