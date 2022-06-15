##############################################
# Variables
##############################################
version=1.0.1
change-list=release
reference=libs/reference-server-0.2.3.jar
ours="libs/robotworld-0.1.0-SNAPSHOT-jar-with-dependencies.jar"
our_server_class="MultiServer"
output="Test_Output"
build_args=""
,:=,
##############################################
# Callables
##############################################
# Runs a java jar file with in.txt as System In
define run_as_jar
	@[ -d $(output) ] || mkdir -p $(output)
	@touch in.txt
	@./run_as_jar.sh "${1}" $(output)
	@echo "[1;33mRunning Jar:[m[1;34m${1}[m"
	@test_running_in=jar
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
	@-mvn test -Dtest="${1}" > "$(output)/Test Results - ${test_running_in} -${1}.txt" || true
	@cat "$(output)/Test Results - ${test_running_in} -${1}.txt" | grep "Tests run" | grep -v "Time elapsed"
endef
# Runs a .java file that has a main using maven with in.txt as System In
define run_with_maven
	@[ -d $(output) ] || mkdir -p $(output)
	@touch in.txt
	@./run_with_maven.sh ${firstword ${1}} "${wordlist 2,${words ${1}},${1}}" $(output)
	@echo "[1;33mRunning with Maven:[m[1;34m${1}[m"
endef
# run test processes as a callable definitions
define run_test_ref
	@$(eval test_running_in="ref")
	$(call run_as_jar, $(reference) $(strip ${2}))
	-$(call test, "$(strip ${1})")
	-$(call close)
	##############################################
endef
define run_test_own
	@$(eval test_running_in="own")
	$(call run_with_maven, $(our_server_class) $(strip ${2}))
	-$(call test, "$(strip ${1})")
	-$(call close)
	##############################################
endef
define run_test
	-$(call run_test_ref, "$(strip ${1})", "$(strip ${2})")
	-$(call run_test_own, "$(strip ${1})", "$(strip ${2})")
endef
##############################################
# Commands
##############################################
help: ## List of commands
	##############################################
	@echo "[1mList of commands:[m"
	@echo " [1;34mbuild[m\t\t\t\tbuilds our project"
	@echo " [1;34mrun_acceptance_tests[m\t\ttests our and the reference projects"
	@echo " [1;34mrelease[m\t\t\tversions and packages our project"
	@echo " [1;34mall[m\t\t\t\tdoes all of the above"
	@echo "    > [1;33margument:[m\tBuild Arguments:\t[1;34m'build_args=\"-Dmaven.test.skip=true\"'[m"
	@echo " - - - - - - - - - - - - - - - - - - - - - - - "
	@echo " [1;34mrun_test[m\t\t\tAllows manual testing"
	@echo " [1;34mrun_test_reference[m\t\tJust on reference server"
	@echo " [1;34mrun_test_own[m\t\t\tJust on own server"
	@echo "    > [1;33margument:[m\tTest Class to Run:\t[1;34m't=\"ConnectionTests\"'[m"
	@echo "    > [1;33margument:[m\tServer Arguments:\t[1;34m'a=\"--size=10\"'[m"
	##############################################

.PHONY: all
all: build run_acceptance_tests release ## Builds, Tests and does versioning
	@echo "[1;32mEverything is complete![m"
	##############################################

.PHONY: build
build: clean init compile verify ## Builds our project
#This will build our project

	@echo "[1;32mProject build complete![m"
	##############################################
clean:
#clear up artifacts from old builds

	-@mvn clean $(build_args)

	@echo "Project has been cleaned."
	##############################################
init:
#idk

	-@mvn initialize $(build_args)

	@echo "Project has been initialized."
	##############################################
compile:
#compiling the project

	-@mvn compile $(build_args)

	@echo "Project has been compiled."
	##############################################
verify:
#verifying the project

	-@mvn verify $(build_args)

	@echo "Project has been verified."
	##############################################

.PHONY: acceptance_tests
run_acceptance_tests:
	##############################################
	-$(call run_test, "LookRobotTests#invalidLookArgumentsShouldFail$(,)ConnectionTests$(,)LaunchRobotTests$(,)StateRobotTests$(,)LookRobotTests#invalidLookCommandShouldFail" , "")
	-$(call run_test, "LookRobotTests#validLookOtherArtifacts", "--size=2 --visibility=2 --obstacle=0$(,)1")
	-$(call run_test, "LookRobotTests#validLookNoOtherArtifacts", "--size=2 --visibility=2")
	@echo "[1;32mAll testing complete![m"
	##############################################

.PHONY: run_test
run_test: run_test_reference run_test_own ## Allows manual testing
#Running tests against both servers in a more dynamic way
	@echo "[1;32mAll testing complete![m"
	##############################################
.PHONY: run_test_reference
run_test_reference: ## Allows manual testing from reference server
	@echo "[1mStarting Run of custom tests on reference server.[m"
	##############################################
	-$(call run_test_ref,"$(t)",$(a))
	@echo "[1mCompleted Run of custom tests on reference server.[m"
	##############################################
.PHONY: run_test_own
run_test_own: ## Allows manual testing from our server
	@echo "[1mStarting Run of custom tests on own server.[m"
	##############################################
	-$(call run_test_own,"$(t)",$(a))
	@echo "[1mCompleted Run of custom tests on own server.[m"
	##############################################

.PHONY: release
release: version_software_for_release package_software_for_release tag_version_number_on_git ## Versions and packages our project
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

	-@mvn -Drevision=$(version) -Dchangelist=-$(change-list)

	@echo "Completed versioning of our software."
	##############################################
package_software_for_release:
#This packages the software for release.
#For now we are skiping the tests

	#mvn package -Dmaven.test.skip=true

	-@mvn clean package $(build_args)

	@echo "Completed packaging of software."
	##############################################
tag_version_number_on_git:
#Tag the version number on git as release-x.y.z
#for software that has been successfully built
#for release. Remember that a successful build
#is one that has passed all acceptance tests
#for this iteration running against the reference
#server and your own server.

	-@git tag -a $(change-list)-$(version) -m "stable version="$(version)

	@echo "Completed tagging version number on git."
	##############################################









































#.PHONY: test
#test: run_acceptance_tests ## Tests our and the reference projects
#	@echo "[1;32mAll testing complete![m"
#	##############################################
#reference_acceptance_tests:
##This is where we will put all the scripting
##In order to run the acceptance tests on
##The Reference Server
#	@$(eval test_running_in="ref")
#	##############################################
#	@echo "[1mStarting Run of acceptance tests on reference server.[m"
#	##############################################
#	$(call run_as_jar, $(reference) --size=1)
#	-$(call test, "ConnectionTests")
#	-$(call test, "LaunchRobotTests")
#	-$(call test, "StateRobotTests")
#	-$(call test, "LookRobotTests#invalidLookCommandShouldFail+invalidLookArgumentsShouldFail")
#	-$(call close)
#	##############################################
#	$(call run_as_jar, $(reference) --size=2 --visibility=2 --obstacle=0$(,)1)
#	-$(call test, "LookRobotTests#validLookOtherArtifacts")
#	-$(call close)
#	##############################################
#	$(call run_as_jar, $(reference) --size=2 --visibility=2)
#	-$(call test, "LookRobotTests#validLookNoOtherArtifacts")
#	-$(call close)
#	##############################################
#	@echo "[1mCompleted Run of acceptance tests on reference server.[m"
#	##############################################
#own_acceptance_tests:
##This is where we will put all the scripting
##In order to run the acceptance tests on
##Our server
#	@$(eval test_running_in="own")
#	@echo "[1mStarting Run of acceptance tests on own server.[m"
#	##############################################
#	$(call run_with_maven, $(our_server_class) --size=1)
#	-$(call test, "ConnectionTests")
#	-$(call test, "LaunchRobotTests")
#	-$(call test, "StateRobotTests")
#	-$(call test, "LookRobotTests#invalidLookCommandShouldFail+invalidLookArgumentsShouldFail")
#	-$(call close)
#	##############################################
#	$(call run_with_maven, $(our_server_class) --size=2 --visibility=2 --obstacle=0$(,)1)
#	-$(call test, "LookRobotTests#validLookOtherArtifacts")
#	-$(call close)
#	##############################################
#	$(call run_with_maven, $(our_server_class) --size=2 --visibility=2)
#	-$(call test, "LookRobotTests#validLookNoOtherArtifacts")
#	-$(call close)
#	##############################################
#	@echo "[1mCompleted Run of acceptance tests on our server.[m"
#	##############################################
