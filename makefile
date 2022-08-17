##############################################
# Variables
##############################################
output="Test_Output"
build_args=""
,:=,
##############################################
# Callables
##############################################
#Runs the test file with the given name
#(add "#methodName" to have it run a single test)
define test
	@[ -d $(output) ] || mkdir -p $(output)
	@echo "[1;33mRunning Test:[m[1;34m${1}[m"
	-@mvn test -Dtest="${1}" > "$(output)/Test Results - ${test_running_in} -${1}.txt" || true
	-@cat "$(output)/Test Results - ${test_running_in} -${1}.txt" | grep "Tests run" | grep -v "Time elapsed"
	-@cat "$(output)/Test Results - ${test_running_in} -${1}.txt" | grep "expected" | grep -v "org.opentest" || true
endef
##############################################
# Commands
##############################################
help: ## List of commands
	##############################################
	@echo "[1mList of commands:[m"
	@echo " [1;34mbuild[m\t\t\t\tbuilds our project"
	@echo " [1;34unit_test[m\t\truns our projects unit tests"
	@echo " [1;34macceptance_test[m\t\truns our projects acceptance tests"
	@echo " [1;34mtest[m\t\truns all our projects tests"
	@echo " [1;34mrelease[m\t\t\tversions and packages our project"
	@echo " [1;34mall[m\t\t\t\tdoes all of the above"
	@echo "    > [1;33margument:[m\tBuild Arguments:\t[1;34m'build_args=\"-Dmaven.test.skip=true\"'[m"
	##############################################
.PHONY: all
all: build test release deploy ## Builds, Tests and does versioning
	@echo "[1;32mEverything is complete![m"
	##############################################
.PHONY: build
build: ## Builds our project
#This will build our project
	@mvn compile
	@echo "[1;32mProject build complete![m"
	##############################################
.PHONY: test
test: unit_test acceptance_test

	@echo "[1;32mProject testing complete![m"
	##############################################
.PHONY: unit_test
unit_test: ## Run unit tests for our project
#This will unit test our project
	@mvn test
	@echo "[1;32mUnit testing complete![m"
	##############################################
.PHONY: acceptance_test
acceptance_test: ## Run acceptance tests for our project
#This will run the acceptance tests for our project
	-$(call test, "ConnectionTests")
	-$(call test, "LaunchTests")
	-$(call test, "LookTests")
	-$(call test, "ForwardTests")
	-$(call test, "StateTests")
	-$(call test, "DatabaseTests")

	@echo "[1;32mAcceptance testing complete![m"
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
