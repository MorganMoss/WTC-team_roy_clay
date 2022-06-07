version=0.0.0
reference=reference-server-0.1.0.jar
ours=server.jar

# Runs a java jar file with in.txt as System In
define run
	@touch in.txt
	@./start.sh "${1}"
	@echo "Running ${1}"
endef

# Sends input to in.txt for running jar
define send_input
	@echo ${1} > in.txt
endef

# exits running jar
define close
	@$(call send_input, "quit")
	@./stop.sh
	@rm in.txt
	@echo "Closed Running Java Application!"
endef

#Runs the test file with the given name
#(add "#methodName" to have it run a single test)
define test
	@echo "Running Test: ${1}"
	@-mvn test -Dtest="${1}" -q
	@echo "Test Complete!"
endef

define runNJ
	@touch in.txt
	@./startNJ.sh "${1}"
	@echo "Running ${1}"
endef

#main: build reference_acceptance_tests own_acceptance_tests version_software_for_release package_software_for_release tag_version_number_on_git
main : build reference_acceptance_tests

build: clean init compile
#This will build our project

#	mvn build

	@echo "Project has been built."
	##############################################
clean:
#clear up artifacts from old builds

	mvn clean

	@echo "Project has been cleaned."
	##############################################
init:
#idk

	mvn initialize

	@echo "Project has been initialized."
	##############################################
compile:
#compiling the project

	mvn compile

	@echo "Project has been compiled."
	##############################################
#TODO: Add your reference tests to this
reference_acceptance_tests:
#This is where we will put all the scripting
#In order to run the acceptance tests on
#The Reference Server
	@echo "Starting Run of acceptance tests on reference server."

	-$(call run, $(reference) --size=1)
	-$(call test, "ConnectionTests")
	-$(call close)

	-$(call run, $(reference) --size=10)
	-$(call test, "LookRobotTests#invalidLookCommandShouldFail")
	-$(call close)

	@echo "Completed Run of acceptance tests on reference server."
	##############################################
#TODO: Need to package our server as a jar before we run acceptance tests on it.
own_acceptance_tests:
#This is where we will put all the scripting
#In order to run the acceptance tests on
#Our server
	@echo "Starting Run of acceptance tests on our server."

	-$(call run, $(ours) --size=1)
	-$(call test, "ConnectionTests")
	-$(call close)

	-$(call run, $(ours) --size=10)
	-$(call test, "LookRobotTests#invalidLookCommandShouldFail")
	-$(call close)

	@echo "Completed Run of acceptance tests on our server."
	##############################################
#TODO
version_software_for_release:
#This must be able to distinguish
#between a release build and a development build.
#For a release build it must remove the SNAPSHOT part
#of the version string in the pom.xml file before doing the build.
#You can use Makefile variables and command line parameters
#to specify whether this is for development or release,
#or you can use different make tasks, or both.
#You can also use different bash scripts too.
#The choice is yours.

	@echo "Completed versioning of our software."
	##############################################
#TODO
package_software_for_release:
#This packages the software for release.

	@echo "Completed packaging of software."
	##############################################
#TODO
tag_version_number_on_git:
#Tag the version number on git as release-x.y.z
#for software that has been successfully built
#for release. Remember that a successful build
#is one that has passed all acceptance tests
#for this iteration running against the reference
#server and your own server.

	@echo "Completed tagging version number on git."
	##############################################