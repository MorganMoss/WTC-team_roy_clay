main: build reference_acceptance_tests own_acceptance_tests version_software_for_release package_software_for_release tag_version_number_on_git

build:
	#This will build our project

    mvn compile

	@echo "Project has been built."
	##############################################

reference_acceptance_tests:
	#This is where we will put all the scripting
	#In order to run the acceptance tests on
	#The Reference Server



	@echo "Completed Run of acceptance tests on reference server."
	##############################################

own_acceptance_tests:
	#This is where we will put all the scripting
	#In order to run the acceptance tests on
	#Our server



	@echo "Completed Run of acceptance tests on our server."
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



	@echo "Completed versioning of our software."
	##############################################

package_software_for_release:
	#This packages the software for release.
	@echo "Completed packaging of software."
	##############################################

tag_version_number_on_git:
	#Tag the version number on git as release-x.y.z
	#for software that has been successfully built
	#for release. Remember that a successful build
	#is one that has passed all acceptance tests
	#for this iteration running against the reference
	#server and your own server.



	@echo "Completed tagging version number on git."
	##############################################