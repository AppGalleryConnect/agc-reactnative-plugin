import * as React from 'react';
import { View, Text, Button } from 'react-native';
import { Separator, Styles } from './separator';
import AGCAuth from '@react-native-agconnect/auth';

var subscription;

function listener(tokenSnapshot) {
    console.log("tokensnapshot update");
};

export default function AnonymouslyScreen() {

    const [current, setCurrent] = React.useState("no user login");
    function getUser() {
        AGCAuth.getInstance().currentUser().then((user) => {
            var info = user ? ("userid=" + user.uid + ", providerId=" + user.providerId):"no user login";
            setCurrent(info);
        });
    }
    React.useEffect(() => { return getUser();}, []);

    return (
        <View style={Styles.sectionContainer}>
            <Text>{current}</Text>
            <Separator />
            <Button
                title="get user"
                onPress={() => {
                    AGCAuth.getInstance().currentUser().then((user) => {
                        console.log(user);
                    });
                }}
            />

            <Separator />
            <Button
                title="signIn"
                onPress={() => {
                    AGCAuth.getInstance().signInAnonymously().then((signInResult) => {
                        getUser();
                    }).catch(err => {
                        console.log(err.code + "  message=" + err.message);
                    });
                }}
            />

            <Separator />
            <Button
                title="add listener"
                onPress={() => {
                    console.log("addTokenListener success");
                    subscription = AGCAuth.getInstance().addTokenListener(listener);
                }}
            />

            <Separator />
            <Button
                title="remove listener"
                onPress={() => {
                    if (subscription) {
                        console.log("removeTokenListener success");
                        AGCAuth.getInstance().removeTokenListener(subscription);
                    }
                }}
            />

            <Separator />
            <Button
                title="signOut"
                onPress={() => {
                    AGCAuth.getInstance().signOut().then(() => {
                        console.log("signOut success");
                        getUser();
                    });
                }}
            />

            <Separator />
            <Button
                title="delete user"
                onPress={() => {
                    AGCAuth.getInstance().deleteUser().then(() => {
                        console.log("delete user success");
                        getUser();
                    });
                }}
            />
        </View>
    );
}



