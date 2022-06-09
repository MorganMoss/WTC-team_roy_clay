# Variables
version=1.0.1
change-list=release
reference=libs/reference-server-0.1.0.jar
ours="libs/robotworld-0.1.0-SNAPSHOT-jar-with-dependencies.jar"
our_server_class="MultiServer"
# This script is very angry about commas as arguments
,:=,


#Callables
# Runs a java jar file with in.txt as System In
define run_as_jar
	@touch in.txt
	@./run_as_jar.sh "${1}"
	@echo "[1;33mRunning Jar:[m[1;34m${1}[m"
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
endef

#Runs the test file with the given name
#(add "#methodName" to have it run a single test)
define test
	@echo "[1;33mRunning Test:[m[1;34m${1}[m"
	@-mvn test -Dtest="${1}" > "Test Results -${1}.txt" || true
	@cat "Test Results -${1}.txt" | grep "Tests run" | grep -v "Time elapsed"
endef

define run_with_maven
	@touch in.txt
	@./run_with_maven.sh ${1}
	@echo "[1;33mRunning with Maven:[m[1;34m${1}[m"
endef


help :
	##############################################
	@echo "[1mList of commands:[m"
	@echo " - [1;34m'make build'[m\tbuilds our project"
	@echo " - [1;34m'make test'[m\ttests our and the reference projects"
	@echo " - [1;34m'make release'[m\tversions and packages our project"
	@echo " - [1;34m'make all'[m\tdoes all of the above"
	@echo " - [1;34m'make run_test'[m\tAllows manual testing"
	@echo "    > [1;33margument:[m\tTest Class to Run:\t[1;34m't=\"ConnectionTests\"'[m"
	@echo "    > [1;33margument:[m\tServer Arguments:\t[1;34m'a=\"--size=10\"'[m"
	@echo " -- [1;33malternate:[m\t[1;34m'make run_test_reference'[m\tJust on reference server"
	@echo " -- [1;33malternate:[m\t[1;34m'make run_test_own'[m\t\tJust on own server"
	##############################################

.PHONY: run_test
run_test: run_test_reference run_test_own
#Running tests against both servers in a more dynamic way
	@echo "[1;32mAll testing complete![m"
	##############################################
.PHONY: run_test_reference
run_test_reference:
	@echo "[1mStarting Run of custom tests on reference server.[m"
	##############################################
	$(call run_as_jar, $(reference) $(a))
	-$(call test, "$(t)")
	-$(call close)
	##############################################
	@echo "[1mCompleted Run of custom tests on reference server.[m"
	##############################################
.PHONY: run_test_own
run_test_own:
	@echo "[1mStarting Run of custom tests on own server.[m"
	##############################################
	$(call run_with_maven, $(our_server_class) $(a))``
	-$(call test, "$(t)")
	-$(call close)
	##############################################
	@echo "[1mCompleted Run of custom tests on own server.[m"
	##############################################


.PHONY: all
all : build test release
	@echo "[1;32mEverything is complete![m"
	##############################################

.PHONY: build
build: clean init compile verify
#This will build our project

	@echo "[1;32mProject build complete![m"
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
verify:
#verifying the project

	mvn verify

	@echo "Project has been verified."
	##############################################

.PHONY: test
test : reference_acceptance_tests own_acceptance_tests
	@echo "[1;32mAll testing complete![m"
	##############################################
reference_acceptance_tests:
#This is where we will put all the scripting
#In order to run the acceptance tests on
#The Reference Server
	##############################################
	@echo "[1mStarting Run of acceptance tests on reference server.[m"
	##############################################
	$(call run_as_jar, $(reference) --size=1)
	-$(call test, "ConnectionTests")
	-$(call test, "LaunchRobotTests")
	-$(call test, "StateRobotTests")
	-$(call test, "LookRobotTests#invalidLookCommandShouldFail")
	-$(call close)
	##############################################
	$(call run_as_jar, $(reference) --size=10 --obstacle=0$(,)1)
	-$(call test, "LookRobotTests#validLookOtherArtifacts")
	-$(call close)
	##############################################
	$(call run_as_jar, $(reference) --size=10)
	-$(call test, "LookRobotTests#validLookNoOtherArtifacts")
	-$(call close)
	##############################################
	@echo "[1mCompleted Run of acceptance tests on reference server.[m"
	##############################################
own_acceptance_tests:
#This is where we will put all the scripting
#In order to run the acceptance tests on
#Our server
	@echo "[1mStarting Run of acceptance tests on own server.[m"
	##############################################
	$(call run_with_maven, $(our_server_class) --size=1)
	-$(call test, "ConnectionTests")
	-$(call test, "LaunchRobotTests")
	-$(call test, "StateRobotTests")
	-$(call test, "LookRobotTests#invalidLookCommandShouldFail")
	-$(call close)
	##############################################
	$(call run_with_maven, $(our_server_class) --size=10 --obstacle=0$(,)1)
	-$(call test, "LookRobotTests#validLookOtherArtifacts")
	-$(call close)
	##############################################
	$(call run_with_maven, $(our_server_class) --size=10)
	-$(call test, "LookRobotTests#validLookNoOtherArtifacts")
	-$(call close)
	##############################################
	@echo "[1mCompleted Run of acceptance tests on our server.[m"
	##############################################

.PHONY: release
release: version_software_for_release package_software_for_release tag_version_number_on_git
	@echo "[1;32mProject packaging and versioning complete![m"
	##############################################
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

	mvn -Drevision=$(version) -Dchangelist=-$(change-list)

	@echo "Completed versioning of our software."
	##############################################
package_software_for_release:
#This packages the software for release.
#For now we are skiping the tests

	#mvn package -Dmaven.test.skip=true

	mvn clean package

	@echo "Completed packaging of software."
	##############################################
tag_version_number_on_git:
#Tag the version number on git as release-x.y.z
#for software that has been successfully built
#for release. Remember that a successful build
#is one that has passed all acceptance tests
#for this iteration running against the reference
#server and your own server.

	git tag -a $(change-list)-$(version) -m "stable version="$(version)

	@echo "Completed tagging version number on git."
	##############################################

