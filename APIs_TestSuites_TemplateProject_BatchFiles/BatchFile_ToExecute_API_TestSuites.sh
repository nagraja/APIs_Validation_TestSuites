#!/bin/bash
#Script_Location=~/Desktop/APIs_Validation_Script_and_Files/ProjectName_APIs_TestSuites


Env_URL_Value=$1
TestType_Value=$2
JJname_Value=$3  
#JJname means JenkinsJobname

#edge.mystifying-varahamihira-76.backbase.eu

####Environment URL Check Starts####
if (( Env_URL_Value == null ))
then
    echo Hello Enter
    read -p 'Enter Environment URL Please: ' Env_URL
    read -p 'Enter Test Type (ST or FT) Please: ' TT_Value
    Script_Location=~/Desktop/APIs_Validation_Script_and_Files/ProjectName_APIs_TestSuites
else
    echo Hello Do Not Enter
    Env_URL=${Env_URL_Value}
    echo Environment URL Value From Jenkins is: $Env_URL
    TT_Value=${TestType_Value}
    echo TestType Value From Jenkins is: $TT_Value
    #Script_Location=~/.jenkins/workspace/API_TestSuites_Jenkins_And_Github_Integration_Job/ProjectName_APIs_TestSuites/
    Script_Location=~/.jenkins/workspace/${JJname_Value}/ProjectName_APIs_TestSuites/
fi

case $TT_Value in
	ST)
		TestSuiteName=STS_File.xml
		;;
	FT)
		TestSuiteName=ListUsers_FT_File.xml
		;;
esac

####Environment URL Check Ends#####
echo User Environment URL Value is: $Env_URL
echo Test Type Value is: $TT_Value
echo Script Location is: $Script_Location

cd ${Script_Location} 
#mvn test -DTestSuiteName="SmokeTestSuite" -DEnvURL="https://reqres.in" -DTestType="ST"

mvn test -DTestSuiteName=${TestSuiteName} -DEnvURL=${Env_URL} -DTestType=${TT_Value}
