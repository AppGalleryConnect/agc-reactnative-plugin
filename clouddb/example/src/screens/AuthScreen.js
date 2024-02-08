/*
 * Copyright 2020-2023. Huawei Technologies Co., Ltd. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from "react";
import {
    StyleSheet,
    TouchableOpacity,
    ScrollView,
    View,
    Text,
    Alert,
    SafeAreaView,
    StatusBar
} from "react-native";
import CustomButton from '../components/CustomButton'
import AGCAuth from '@hw-agconnect/react-native-auth'
import Header from "../components/Header";

const AuthScreen = (props) => {

    const loginPhone = () => {
        AGCAuth.getInstance().signInAnonymously().then(response => {
            Alert.alert("Success", "Logged in  successfully.",
                [
                    { text: 'Okey.', onPress: () => props.navigate("CloudDBScreen") }
                ])
        }).catch(error => {
            Alert.alert("error: ", JSON.stringify(error.message))
        })
    }

    const signOut = () => {
        AGCAuth.getInstance().signOut().then(response => {
            Alert.alert("Success", "Signed out  successfully.",
                [
                    { text: 'Okey.', onPress: () => props.navigate("CloudDBScreen") }
                ])
        }).catch(err => {
            Alert.alert("Error", err.message)
        })
    }

    return (
        <SafeAreaView style={styles.safeArea}>
            <StatusBar barStyle={'light-content'} backgroundColor="#1c1c1c" />
            <View style={styles.container}>
                <Header />
                <TouchableOpacity
                    style={styles.backButton}
                    onPress={() => props.navigate("CloudDBScreen")}>
                    <Text style={styles.backButtonText}> Back </Text>
                </TouchableOpacity>
                <ScrollView>
                    <CustomButton
                        title={"Sign In Anonymously"}
                        onPress={() => loginPhone()}
                    />
                    <CustomButton
                        title={"Sign Out"}
                        onPress={() => signOut()}
                    />
                </ScrollView>
            </View>
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#1c1c1c",
    },
    safeArea: {
        flex: 1,
        backgroundColor: "#1c1c1c",
    },
    textInputStyle: {
        backgroundColor: "#2e2e2e",
        color: "white",
        borderRadius: 5,
        height: 50,
        margin: 10,
        padding: 10,
    },
    phoneInputContainer: {
        height: 60,
        flexDirection: 'row',
        width: '100%',
        marginBottom: 10,
    },
    backButton: {
        position: "absolute",
        top: 10,
        left: 10,
    },
    backButtonText: {
        color: "#34abeb",
        fontSize: 20,
    }
});

export default AuthScreen;
