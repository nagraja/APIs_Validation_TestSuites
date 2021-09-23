#!/bin/bash
Script_Location=~/Desktop/APIs_Validation_Script_and_Files/ProjectName_APIs_TestSuites
#TestSuiteName=STS_File.xml

Env_URL_Value=$1
TestType_Value=$2

#edge.mystifying-varahamihira-76.backbase.eu

####Environment URL Check Starts####
if (( Env_URL_Value == null ))
then
    echo Hello Enter
    read -p 'Enter Environment URL Please: ' Env_URL
    read -p 'Enter Test Type (ST or FT) Please: ' TT_Value
else
    echo Hello Don Not Enter
    Env_URL=${Env_URL_Value}
    echo Environment URL Value From Jenkins is: $Env_URL
    TT_Value=${TestType_Value}
    echo TestType Value From Jenkins is: $TT_Value
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
cd ${Script_Location} 
mvn test -DTestSuiteName=${TestSuiteName} -DEnvURL=${Env_URL} -DTestType=${TT_Value}
