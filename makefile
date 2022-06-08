version=0.0.0
reference=reference-server-0.1.0.jar
ours=libs/robotworld-0.1.0-SNAPSHOT-jar-with-dependencies.jar
# This script is very angry about commas as arguments
,:=,

ifeq (run_test,$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "run"
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(RUN_ARGS):;@:)
endif

# Runs a java jar file with in.txt as System In
define run
	@touch in.txt
	@./start.sh "${1}"
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
	@echo "[1;37mClosed Running Java Process[m"
endef

#Runs the test file with the given name
#(add "#methodName" to have it run a single test)
define test
	@echo "[1;33mRunning Test:[m[1;34m${1}[m"
	@-mvn test -Dtest="${1}" > "Test Results -${1}.txt" || true
	@cat "Test Results -${1}.txt" | grep "Tests run" | grep -v "Time elapsed"
endef

define runNJ
	@touch in.txt
	@./startNJ.sh ${1}
	@echo "[1;33mRunning with Maven:[m[1;34m${1}[m"
endef


help :
	##############################################
	@echo "[1mList of commands:[m"
	@echo " - [1;34m'make build'[m\tbuilds our project"
	@echo " - [1;34m'make test'[m\ttests our and the reference projects"
	@echo " - [1;34m'make all'[m\tdoes all of the above"
	@echo " - [1;34m'make run_test <Test Class#Method>'[m\tAllows manual testing"
	##############################################

run_test:
	##############################################
	@echo "[1mStarting Run of custom tests on reference server.[m"
	##############################################
	$(call run, $(reference) --size=1)
	-$(call test, "$(RUN_ARGS)")
	-$(call close)
	##############################################
	@echo "[1mCompleted Run of custom tests on reference server.[m"
	##############################################
	@echo "[1mStarting Run of custom tests on own server.[m"
	##############################################
	$(call runNJ, $(own) --size=1)
	-$(call test, "$(RUN_ARGS)")
	-$(call close)
	##############################################
	@echo "[1mCompleted Run of custom tests on own server.[m"
	##############################################
	@echo "[1;32mAll testing complete![m"
	##############################################

all : build test
	##############################################

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
	$(call run, $(reference) --size=1)
	-$(call test, "ConnectionTests")
	-$(call test, "LaunchRobotTests")
	-$(call test, "StateRobotTests")
	-$(call test, "LookRobotTests#invalidLookCommandShouldFail")
	-$(call close)
	##############################################
	$(call run, $(reference) --size=10 --obstacle=0$(,)1)
	-$(call test, "LookRobotTests#validLookOtherArtifacts")
	-$(call close)
	##############################################
	$(call run, $(reference) --size=10)
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
	$(call runNJ, $(ours) --size=1)
	-$(call test, "ConnectionTests")
	-$(call test, "LaunchRobotTests")
	-$(call test, "StateRobotTests")
	-$(call test, "LookRobotTests#invalidLookCommandShouldFail")
	-$(call close)
	##############################################
	$(call runNJ, $(ours) --size=10 --obstacle=0$(,)1)
	-$(call test, "LookRobotTests#validLookOtherArtifacts")
	-$(call close)
	##############################################
	$(call runNJ, $(ours) --size=10)
	-$(call test, "LookRobotTests#validLookNoOtherArtifacts")
	-$(call close)
	##############################################
	@echo "[1mCompleted Run of acceptance tests on our server.[m"
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

	#mvn clean deploy -Drevision=".$TRAVIS_BUILD_ID"
	mvn versions:set -DnewVersion=1.0.0-${revision}

	@echo "Completed versioning of our software."
	##############################################
package_software_for_release:
#This packages the software for release.
#For now we are skiping the tests

	mvn package -Dmaven.test.skip=true

	mvn package

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

