##############################################
# Variables
##############################################
version=1.0.1-SNAPSHOT
reference=libs/reference-server-0.2.3.jar
ours=libs/Server-$(version).jar
our_server_class="Server"
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
	@cat "$(output)/Test Results - ${test_running_in} -${1}.txt" | grep "expected" | grep -v "org.opentest"
endef
# Runs a .java file that has a main using maven with in.txt as System In
define run_with_maven
	@[ -d $(output) ] || mkdir -p $(output)
	@touch in.txt
	@./run_with_maven.sh ${firstword ${1}} "${wordlist 2,${words ${1}},${1}}" $(output) $(server_module)
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
	$(call run_as_jar, $(ours) $(strip ${2}))
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
	@echo $(ours)
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
all: build test package run_acceptance_tests release deploy ## Builds, Tests and does versioning
	@echo "[1;32mEverything is complete![m"
	##############################################

.PHONY: build
build: ## Builds our project
#This will build our project
	@mvn compile
	@echo "[1;32mProject build complete![m"
	##############################################

.PHONY: test
test: ## Run unit tests for our project
#This will unit test our project
	@mvn test
	@echo "[1;32mProject testing complete![m"
	##############################################

.PHONY: package
package: package_software_for_testing ## Packaging for acceptance tests
	@echo "[1;32mContinuous integration and packaging complete![m"
	##############################################
package_software_for_testing:
#This packages the software for release.

	-@mvn clean install

	@echo "Completed packaging of software for testing."
	##############################################

.PHONY: run_acceptance_tests
run_acceptance_tests:
	##############################################
#	$(,)LookRobotTests#invalidLookCommandShouldFail$(,)LookRobotTests#invalidLookArgumentsShouldFail
	-$(call run_test, "ConnectionTests$(,)LaunchRobotTests#validLaunchShouldSucceed+invalidLaunchShouldFail+checkForDuplicateRobotName+worldFullNoSpaceToLaunchRobot$(,)StateRobotTests" , "--size=1")
#	-$(call run_test, "LookRobotTests#validLookOtherArtifacts", "--size=2 --visibility=2 --obstacle=0$(,)1")
#	-$(call run_test, "LookRobotTests#validLookNoOtherArtifacts", "--size=2 --visibility=2")
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
release: version_software_for_release ## Versions and packages our project
	@echo "[1;32mProject release complete![m"
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

	-@mvn -Dchangelist=RELEASE

	@echo "Completed versioning of our software."
	##############################################

.PHONY: deploy
deploy: package_software_for_release tag_version_number_on_git ## Versions and packages our project
	@echo "[1;32mContinuous deployment and tagging complete![m"
	##############################################
package_software_for_release:
#This packages the software for release.

	-@mvn clean package $(build_args)

	@echo "Completed packaging of software for release."
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






































##This is where we will put all the scripting
##In order to run the acceptance tests on
##The Reference Server

##This is where we will put all the scripting
##In order to run the acceptance tests on
##Our server
