/*
 * Copyright (c) 2020-2023. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
import React, { useEffect, useState } from 'react';
import {
  SafeAreaView,
  ScrollView,
  PermissionsAndroid,
  ToastAndroid,
  StyleSheet,
  StatusBar,
  Platform,
  Alert,
} from 'react-native';
import CustomButton from './src/components/CustomButton';
import Header from './src/components/Header';
import AGCStorage, {
  ListResult, FileMetadata, TaskUri
} from '@hw-agconnect/react-native-storage';
import { convertToByte } from "./src/utils/convertByteUtils";

const App = () => {
  const [permission, setPermission] = useState(false);
  const [storageManagement, setStorageManagement] = useState(null);
  const [storageReference, setStorageReference] = useState(null);
  const [fileUploadTask, setUploadTask] = useState(null);
  const [fileDownloadTask, setDownloadTask] = useState(null);

  const toast = (msg) => {
    if (Platform.OS === "android") {
      ToastAndroid.show(msg, ToastAndroid.SHORT);
    }
    else {
      Alert.alert(msg);
    }
  };

  const log = (tag, msg) => {
    console.log(tag, msg);
    toast(tag + " " + msg);
  };

  const requestPermission = async () => {
    try {
      const userResponse = await PermissionsAndroid.requestMultiple([
        PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
        PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE,
      ]);

      if (
        userResponse['android.permission.READ_EXTERNAL_STORAGE'] ===
        PermissionsAndroid.RESULTS.DENIED ||
        userResponse['android.permission.READ_EXTERNAL_STORAGE'] ===
        PermissionsAndroid.RESULTS.NEVER_ASK_AGAIN ||
        userResponse['android.permission.WRITE_EXTERNAL_STORAGE'] ===
        PermissionsAndroid.RESULTS.DENIED ||
        userResponse['android.permission.WRITE_EXTERNAL_STORAGE'] ===
        PermissionsAndroid.RESULTS.NEVER_ASK_AGAIN
      ) {
        console.log('permissions denied!');
        setPermission(false);
      } else {
        console.log('permissions ok!');
        setPermission(true);
      }
    } catch (err) {
      console.warn(err);
    }
  };


  useEffect(() => {
    if (Platform.OS === "android" && !permission) {
      requestPermission();
    }
  }, []);


  const getInstance = () => {
    AGCStorage.getInstance()
      .then((response) => {
        log('getInstance', JSON.stringify(response));
        setStorageManagement(response);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const getInstanceWithRoute = () => {
    const agcConfig = {
      bucketName: '<bucket_name>',
      routePolicy: AGCStorage.AGCRoutePolicy.CHINA,
    };

    AGCStorage.getInstance(agcConfig)
      .then((response) => {
        log('getInstance', JSON.stringify(response));
        setStorageManagement(response);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const getStorageReference = () => {
    storageManagement
      .getStorageReference("test717.pdf")
      .then((result) => {
        log('getStorageReference :: success ', JSON.stringify(result, null, 1));
        setStorageReference(result);
      })
      .catch((error) =>
        log('getStorageReference :: Error! ' + JSON.stringify(error, null, 1))
      );
  };

  const getReferenceFromUrl = () => {
    const params = {
      routePolicy: AGCStorage.AGCRoutePolicy.CHINA,
      fullUrl:
        'fullUrl',
    };
    storageManagement
      .getReferenceFromUrl(params)
      .then((result) => {
        log('getReferenceFromUrl :: success ', JSON.stringify(result, null, 1));
        setStorageReference(result);
      })
      .catch((error) =>
        log('getReferenceFromUrl :: Error! ', JSON.stringify(error, null, 1))
      );
  };

  const getAGCStorageManagementDetail = async () => {
    let uploadTimeout = await storageManagement.getMaxUploadTimeout();
    let maxDownloadTimeout = await storageManagement.getMaxDownloadTimeout();
    let maxRequestTimeout = await storageManagement.getMaxRequestTimeout();
    let retryTimes = await storageManagement.getRetryTimes();
    let agcStorageManagementDetailJSON = {
      uploadTimeout: uploadTimeout,
      maxDownloadTimeout: maxDownloadTimeout,
      maxRequestTimeout: maxRequestTimeout,
      retryTimes: retryTimes
    };
    log(
      'getAGCStorageManagementDetail :: success \n',
      JSON.stringify(agcStorageManagementDetailJSON, null, 1)
    );
  };

  const setMaxUploadTimeout = () => {
    storageManagement
      .setMaxUploadTimeout('61234')
      .then(() => {
        log('setMaxUploadTimeout ::  ', "success");
      })
      .catch((error) =>
        log('setMaxUploadTimeout :: Error! ', JSON.stringify(error, null, 1))
      );
  };

  const setMaxDownloadTimeout = () => {
    storageManagement
      .setMaxDownloadTimeout('1234')
      .then(() => {
        log('setMaxDownloadTimeout ::  ', "success");
      })
      .catch((error) =>
        log(
          'setMaxDownloadTimeout :: Error! ', JSON.stringify(error, null, 1)
        )
      );
  };

  const setMaxRequestTimeout = () => {
    storageManagement
      .setMaxRequestTimeout('1234')
      .then(() => {
        log('setMaxRequestTimeout ::  ', "success");
      })
      .catch((error) =>
        log(
          'setMaxRequestTimeout :: Error! ', JSON.stringify(error, null, 1)
        )
      );
  };

  const setRetryTimes = () => {
    storageManagement
      .setRetryTimes(10089)
      .then(() => {
        log('setRetryTimes ::', "success");
      })
      .catch((error) =>
        log(
          'setRetryTimes :: Error! ', JSON.stringify(error, null, 1)
        )
      );
  };

  const getStorage = () => {
    log("getStorage", JSON.stringify(storageReference.getStorage()));
  };

  const getChild = () => {
    storageReference
      .child('test')
      .then((res) => {
        log('getChild', JSON.stringify(res, null, 1));
      })
      .catch((error) =>
        log('getChild :: Error! ', JSON.stringify(error, null, 1))
      );
  };

  const getParent = () => {
    storageReference
      .getParent('test')
      .then((res) => {
        log('getParent', JSON.stringify(res, null, 1));
      })
      .catch((error) =>
        log('getParent :: Error! ', JSON.stringify(error, null, 1))
      );
  };

  const getRoot = () => {
    storageReference
      .getRoot()
      .then((res) => {
        log('getRoot', JSON.stringify(res, null, 1));
      })
      .catch((error) =>
        log('getRoot :: Error! ', JSON.stringify(error, null, 1))
      );
  };

  const getStorageReferenceDetail = async () => {
    let bucket = await storageReference.getBucket();
    let name = await storageReference.getName();
    let path = await storageReference.getPath();
    let toString = storageReference.toString();
    let sfDetailJSON = {
      bucket: bucket,
      name: name,
      path: path,
      toString: toString,
    };
    log(
      'getStorageReferenceDetail :: success \n',
      JSON.stringify(sfDetailJSON, null, 1)
    );
  };

  const getFileMetadata = async () => {
    storageReference
      .getFileMetadata()
      .then((taskFileMetadata) => {
        log("taskFileMetadata", JSON.stringify(taskFileMetadata));
        taskFileMetadata.addOnSuccessListener(async (res) => {
          console.log("res", res);
          const fileMetadata = new FileMetadata(res.fileMetadataId);
          let bucket = await fileMetadata.getBucket();
          let cTime = await fileMetadata.getCTime();
          let mTime = await fileMetadata.getMTime();
          let name = await fileMetadata.getName();
          let path = await fileMetadata.getPath();
          let size = await fileMetadata.getSize();
          let sha256Hash = await fileMetadata.getSHA256Hash();
          let contentType = await fileMetadata.getContentType();
          let cacheControl = await fileMetadata.getCacheControl();
          let contentDisposition = await fileMetadata.getContentDisposition();
          let contentLanguage = await fileMetadata.getContentLanguage();
          let contentEncoding = await fileMetadata.getContentEncoding();
          let customMetadata = await fileMetadata.getCustomMetadata();
          let fileMetadataJSON = {
            bucket: bucket,
            cTime: cTime,
            mTime: mTime,
            name: name,
            path: path,
            size: size,
            sha256Hash: sha256Hash,
            contentType: contentType,
            cacheControl: cacheControl,
            contentDisposition: contentDisposition,
            contentLanguage: contentLanguage,
            contentEncoding: contentEncoding,
            customMetadata: customMetadata,
          };
          log(
            'getFileMetadata :: success ',
            JSON.stringify(fileMetadataJSON, null, 1)
          );
        });
      })
      .catch((error) =>
        log('getFileMetadata :: Error! ', JSON.stringify(error, null, 1))
      );
  };

  const updateFileMetadata = async () => {
    let uploadFileMetadata = await FileMetadata.create();
    await uploadFileMetadata.setContentType('file/mp4');
    await uploadFileMetadata.setCacheControl('no-store');
    await uploadFileMetadata.setContentDisposition('attachment;filename=abc.jpg');
    await uploadFileMetadata.setContentEncoding('identity');
    await uploadFileMetadata.setContentLanguage('en-US');
    await uploadFileMetadata.setCustomMetadata({ CustomMetadata123: '123', CustomMetadataTest: 'test', });

    storageReference
      .updateFileMetadata(uploadFileMetadata.fileMetadataId)
      .then(async (taskFileMetadata) => {
        log(
          'updateFileMetadata :: success ',
          JSON.stringify(taskFileMetadata, null, 1)
        );
        setTaskFileMetadataListener(taskFileMetadata);
      })
      .catch((error) =>
        log('updateFileMetadata :: Error! ', JSON.stringify(error, null, 1))
      );
  };

  const setTaskFileMetadataListener = (uptTaskFileMetadata) => {
    uptTaskFileMetadata.addOnSuccessListener((res) => {
      log(
        'uptTaskFileMetadata : addOnSuccessListener: success ',
        JSON.stringify(res.fileMetadataId)
      );
    });
    uptTaskFileMetadata.addOnFailureListener((res) => {
      log(
        'uptTaskFileMetadata : addOnFailureListener: success ',
        JSON.stringify(res)
      );
    });
    uptTaskFileMetadata.addOnCompleteListener((res) => {
      log(
        'uptTaskFileMetadata : addOnCompleteListener: success ',
        JSON.stringify(res.taskFileMetadataId)
      );
    });
    uptTaskFileMetadata.addOnCanceledListener((res) => {
      log('uptTaskFileMetadata : addOnCanceledListener: success ');
    });
  };

  const deleteFile = async () => {
    storageReference.delete()
      .then(() => {
        log("delete ::", "success");
      }).catch((error) => log("delete :: Error! ", JSON.stringify(error, null, 1)));

  };

  const list = () => {
    let max = 15;
    storageReference.list(max).then((res) => {
      log("taskListResult", JSON.stringify(res));
      setListResultSuccessListener(res);
    }).catch((error) => {
      log("error", JSON.stringify(error));
    });
  };

  const listAll = () => {
    storageReference.listAll().then((res) => {
      log("taskListResult", JSON.stringify(res));
      setListResultSuccessListener(res);
    }).catch((error) => {
      log("error", JSON.stringify(error));
    });
  };

  function setListResultSuccessListener(taskListResult) {
    taskListResult.addOnSuccessListener((res) => {
     const listResult = new ListResult(res.listResultId);

      listResult.getFileList().then((storageReferenceArray) => {
        log('getFileList', JSON.stringify(storageReferenceArray));
      })
        .catch((err) => {
          log("getFileList :: Error!", JSON.stringify(err));
        });

      listResult.getPageMarker().then((res) => {
        log('getPageMarker', JSON.stringify(res));
      })
        .catch((err) => {
          log("getPageMarker :: Error!", JSON.stringify(err));
        });

      listResult
        .getDirList()
        .then((storageReferenceArray) => {
          log('getDirList', JSON.stringify(storageReferenceArray));
        })
        .catch((err) => {
          log("getDirList :: Error!", JSON.stringify(err));
        });
    });

    taskListResult.addOnFailureListener((err) => {
      log("addOnFailureListener :: Error!", JSON.stringify(err));
    });
  }

  const getDownloadUrl = async () => {
    storageReference.getDownloadUrl()
      .then((res) => {
        res.addOnCompleteListener(async (resUrl) => {
          log("getDownloadUrl :: success  ", JSON.stringify(resUrl));
          const taskUri = new TaskUri(res.taskUriId);
          log("isComplete", JSON.stringify(await taskUri.isComplete()));
          log("isSuccessful", JSON.stringify(await taskUri.isSuccessful()));
          log("isCanceled", JSON.stringify(await taskUri.isCanceled()));
        });
        res.addOnSuccessListener(async (resUrl) => {
          log("addOnSuccessListener :: success  ", JSON.stringify(resUrl));
        });
      }).catch((error) => log("getDownloadUrl :: Error! ", JSON.stringify(error, null, 1)));
  };

  const getActiveUploadTasks = async () => {
    storageReference.getActiveUploadTasks()
      .then((res) => {
        log("getActiveUploadTasks :: success ActiveUploadTask Size: ", res.length.toString());
      }).catch((error) => log("getActiveUploadTasks :: Error! ", JSON.stringify(error, null, 1)));

  };

  const getActiveDownloadTasks = async () => {
    storageReference.getActiveDownloadTasks()
      .then((res) => {
        log("getActiveDownloadTasks :: success ActiveDownloadTask Size: ", res.length.toString());
      }).catch((error) => log("getActiveDownloadTasks :: Error! ", JSON.stringify(error, null, 1)));
  };

  const equals = () => {
    storageReference.equals("")
      .then((res) => {
        log("equals :: success ", JSON.stringify(res));
      }).catch((error) => log("equals :: Error! ", JSON.stringify(error, null, 1)));
  };

  const hashCode = () => {
    storageReference.hashCode()
      .then((res) => {
        log("hashCode :: success ", JSON.stringify(res));
      }).catch((error) => log("hashCode :: Error! ", JSON.stringify(error, null, 1)));
  };

  const putFile = async () => {
    const inputFile = "/storage/emulated/0/AGCSdk/test717.pdf";
    let uploadFileMetadata = await FileMetadata.create();
    await uploadFileMetadata.setContentType("file/pdf");
    await uploadFileMetadata.setCacheControl("no-store");
    await uploadFileMetadata.setContentDisposition("attachment;filename=test.pdf");
    await uploadFileMetadata.setContentEncoding("identity");
    await uploadFileMetadata.setContentLanguage("en-US");

    storageReference.putFile(inputFile, uploadFileMetadata)
      .then((res) => {
        log("putFile :: success ", JSON.stringify(res));
        //set listener
        setUploadTask(res);
        setUploadTaskListener(res);
      }).catch((error) => log("putFile :: Error! ", JSON.stringify(error, null, 1)));
  };

  const putBytes = async () => {
    let fileMetadata = await FileMetadata.create();
    await fileMetadata.setContentType("file/byte");
    await fileMetadata.setCacheControl("no-store");
    await fileMetadata.setContentDisposition("attachment;filename=test.byte");
    await fileMetadata.setContentEncoding("identity");
    await fileMetadata.setContentLanguage("en-US");
    const str = "Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
        "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an" +
        "unknown printer took a galley of type and scrambled it to make a type specimen book. " +
        "It has survived not only five centuries, but also the leap into electronic typesetting, " +
        "remaining essentially unchanged. It was popularised in the 1960s with the release of " +
        "Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing" +
        " software like Aldus PageMaker including versions of Lorem Ipsum. 界一際人……著產家的語手會保";

    const bytes = convertToByte(str);

    storageReference.putBytes(bytes, fileMetadata)
      .then((res) => {
        log("putBytes :: success ", JSON.stringify(res));
        setUploadTaskListener(res);
      }).catch((error) => log("putBytes :: Error! ", JSON.stringify(error, null, 1)));
  };

  //upload
  function setUploadTaskListener(uploadTask) {
    uploadTask.addOnCanceledListener((res) => {
      log("addOnCanceledListener ", JSON.stringify(res));
    });
    uploadTask.addOnCompleteListener((res) => {
      log("addOnCompleteListener ", JSON.stringify(res));
    });
    uploadTask.addOnFailureListener((res) => {
      log("addOnFailureListener ", JSON.stringify(res));
    });
    uploadTask.addOnSuccessListener((res) => {
      log("addOnSuccessListener ", JSON.stringify(res));
    });
    uploadTask.addOnPausedListener((res) => {
      log("addOnPausedListener ", JSON.stringify(res));
    });
    uploadTask.addOnProgressListener((res) => {
      let percent = (res.bytesTransferred * 100.0) / res.totalByteCount;
      console.log("addOnProgressListener :: progress : %" + percent);
    });
    log("setUploadTaskListener", JSON.stringify(uploadTask));
  }

  const getFile = async () => {
    let ms = (new Date()).getMilliseconds();
    storageReference.getFile("/storage/emulated/0/AGCSdk/abc" + ms + ".pdf")
      .then((res) => {
        log("getFile :: success ", JSON.stringify(res));
        setDownloadTask(res);
        //set listener
        setDownloadTaskListener(res);
      }).catch((error) => log("getFile :: Error! ", JSON.stringify(error, null, 1)));
  };

  const getBytes = () => {
    storageReference.getBytes("100000000")
      .then((res) => {
        log("getBytes :: success", JSON.stringify(res));
        let getByteDownloadTask = res;
        //set listener
        getByteDownloadTask.addOnSuccessListener((res) => {
          log('addOnSuccessListener', JSON.stringify(res));
        });
        getByteDownloadTask.addOnCompleteListener((res) => {
          log("addOnCompleteListener ", JSON.stringify(res));
        });
        getByteDownloadTask.addOnCanceledListener((res) => {
          log("addOnCanceledListener ", JSON.stringify(res));
        });
        getByteDownloadTask.addOnFailureListener((res) => {
          log("addOnFailureListener ", JSON.stringify(res));
        });
      }).catch((error) => log("getBytes :: Error! ", JSON.stringify(error, null, 1)));
  };

  //download
  function setDownloadTaskListener(downloadTask) {
    downloadTask.addOnCanceledListener((res) => {
      log("addOnCanceledListener", JSON.stringify(res));
    });
    downloadTask.addOnCompleteListener((res) => {
      log("addOnCompleteListener ", JSON.stringify(res));
    });
    downloadTask.addOnFailureListener((res) => {
      log("addOnFailureListener ", JSON.stringify(res));
    });
    downloadTask.addOnSuccessListener((res) => {
      log("addOnSuccessListener ", JSON.stringify(res));
    });
    downloadTask.addOnPausedListener((res) => {
      log("addOnPausedListener ", JSON.stringify(res));
    });
    downloadTask.addOnProgressListener((res) => {
      let percent = (res.bytesTransferred * 100.0) / res.totalByteCount;
      console.log("addOnProgressListener :: progress : %" + percent);
    });
    log("setDownloadTaskListener", JSON.stringify(downloadTask));
  }
  const downloadPause = async () => {
    fileDownloadTask.pause()
      .then((res) => {
        log("pause :: success ", JSON.stringify(res));
      }).catch((error) => log("pause :: Error! ", JSON.stringify(error, null, 1)));
  };

  const downloadResume = async () => {
    fileDownloadTask.resume()
      .then((res) => {
        log("resume :: success ", JSON.stringify(res));
      }).catch((error) => log("resume :: Error! ", JSON.stringify(error, null, 1)));
  };

  const downloadCancel = async () => {
    fileDownloadTask.cancel()
      .then((res) => {
        log("cancel :: success ", JSON.stringify(res));
      }).catch((error) => log("cancel :: Error! ", JSON.stringify(error, null, 1)));
  };

  const downloadStatus = async () => {
    if (!fileDownloadTask) {
      log("Empty :: fileDownloadTask");
      return;
    }
    let isCanceled = await fileDownloadTask.isCanceled();
    let isComplete = await fileDownloadTask.isComplete();
    let isSuccessful = await fileDownloadTask.isSuccessful();
    let isInProgress = await fileDownloadTask.isInProgress();
    let downloadStatusJSON = {
      "isCanceled": isCanceled, "isComplete": isComplete, "isSuccessful": isSuccessful, "isInProgress": isInProgress
    };
    log("downloadStatus :: success \n", JSON.stringify(downloadStatusJSON, null, 1));
  };

  const uploadPause = async () => {
    fileUploadTask.pause()
      .then((res) => {
        log("pause :: success ", JSON.stringify(res));
      }).catch((error) => log("pause :: Error! ", JSON.stringify(error, null, 1)));
  };

  const uploadResume = async () => {
    fileUploadTask.resume()
      .then((res) => {
        log("resume :: success ", JSON.stringify(res));
      }).catch((error) => log("resume :: Error! ", JSON.stringify(error, null, 1)));
  };

  const uploadCancel = async () => {
    fileUploadTask.cancel()
      .then((res) => {
        log("cancel :: success ", JSON.stringify(res));
      }).catch((error) => log("cancel :: Error! ", JSON.stringify(error, null, 1)));
  };


  const uploadStatus = async () => {
    let isCanceled = await fileUploadTask.isCanceled();
    let isComplete = await fileUploadTask.isComplete();
    let isSuccessful = await fileUploadTask.isSuccessful();
    let isInProgress = await fileUploadTask.isInProgress();
    let uploadStatusJSON = {
      "isCanceled": isCanceled, "isComplete": isComplete, "isSuccessful": isSuccessful, "isInProgress": isInProgress
    };
    log("uploadStatus :: success \n", JSON.stringify(uploadStatusJSON, null, 1));
  };



  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle={'dark-content'} backgroundColor='#e1e1e1' />
      <Header
        enableBackButton={true}
        onBackPress={() => props.navigate('LoginScreen')}
      />
      <ScrollView style={styles.container}>

        {Platform.OS === 'android' ? <CustomButton onPress={() => requestPermission()} title={'Request Permission'} /> : null}
        <CustomButton onPress={() => getInstance()} title={'Get Instance'} />
        <CustomButton onPress={() => getInstanceWithRoute()} title={'Get Instance With Route'} />
        <CustomButton onPress={() => getStorageReference()} title={'Get Storage Reference'} isDisabled={!storageManagement} />
        <CustomButton onPress={() => getReferenceFromUrl()} title={'Get Storage Reference From Url'} isDisabled={!storageManagement} />
        <CustomButton onPress={() => getAGCStorageManagementDetail()} title={'Get Storage Management Detail'} isDisabled={!storageManagement} />
        <CustomButton onPress={() => setMaxUploadTimeout()} title={'Set Max UploadTimeout'} isDisabled={!storageManagement} />
        <CustomButton onPress={() => setMaxDownloadTimeout()} title={'Set Max Download Timeout'} isDisabled={!storageManagement} />
        <CustomButton onPress={() => setMaxRequestTimeout()} title={'Set Max Request Timeout'} isDisabled={!storageManagement} />
        <CustomButton onPress={() => setRetryTimes()} title={'Set Retry Times'} isDisabled={!storageManagement} />
        <CustomButton onPress={() => getStorage()} title={'Get Storage Management'} isDisabled={!storageReference} />
        <CustomButton onPress={() => getChild()} title={'Get Child'} isDisabled={!storageReference} />
        <CustomButton onPress={() => getParent()} title={'Get Parent'} isDisabled={!storageReference} />
        <CustomButton onPress={() => getRoot()} title={'Get Root'} isDisabled={!storageReference} />
        <CustomButton onPress={() => getStorageReferenceDetail()} title={'Get Storage Reference Details'} isDisabled={!storageReference} />
        <CustomButton onPress={() => getFileMetadata()} title={'Get File Metada'} isDisabled={!storageReference} />
        <CustomButton onPress={() => updateFileMetadata()} title={'Update File Metada'} isDisabled={!storageReference} />
        <CustomButton onPress={() => deleteFile()} title={'Delete File'} isDisabled={!storageReference} />
        <CustomButton onPress={() => list()} title={'List'} isDisabled={!storageReference} />
        <CustomButton onPress={() => listAll()} title={'List All'} isDisabled={!storageReference} />
        <CustomButton onPress={() => getDownloadUrl()} title={'Get Download Url'} isDisabled={!storageReference} />
        <CustomButton onPress={() => getActiveUploadTasks()} title={'Get Active Upload Tasks'} isDisabled={!storageReference} />
        <CustomButton onPress={() => getActiveDownloadTasks()} title={'Get Active Download Tasks'} isDisabled={!storageReference} />
        <CustomButton onPress={() => equals()} title={'Check is Equal?'} isDisabled={!storageReference} />
        <CustomButton onPress={() => hashCode()} title={'Get Hash Code'} isDisabled={!storageReference} />
        <CustomButton onPress={() => putFile()} title={'Put File'} isDisabled={!storageReference} />
        <CustomButton onPress={() => putBytes()} title={'Put Bytes'} isDisabled={!storageReference} />
        <CustomButton onPress={() => getFile()} title={'Get File'} isDisabled={!storageReference} />
        <CustomButton onPress={() => getBytes()} title={'Get Bytes'} isDisabled={!storageReference} />
        <CustomButton onPress={() => downloadPause()} title={'Pause Download'} isDisabled={!fileDownloadTask} />
        <CustomButton onPress={() => downloadResume()} title={'Resume Download'} isDisabled={!fileDownloadTask} />
        <CustomButton onPress={() => downloadCancel()} title={'Cancel Download'} isDisabled={!fileDownloadTask} />
        <CustomButton onPress={() => downloadStatus()} title={'Download Status In'} isDisabled={!fileDownloadTask} />
        <CustomButton onPress={() => uploadPause()} title={'Pause Upload'} isDisabled={!fileUploadTask} />
        <CustomButton onPress={() => uploadResume()} title={'Resume Upload'} isDisabled={!fileUploadTask} />
        <CustomButton onPress={() => uploadCancel()} title={'Cancel Upload'} isDisabled={!fileUploadTask} />
        <CustomButton onPress={() => uploadStatus()} title={'Upload Status'} isDisabled={!fileUploadTask} />
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#e1e1e1',
  },
  container: {
    flex: 1,
    backgroundColor: '#e1e1e1',
    padding: 10,
  },
  phoneInputContainer: {
    width: '100%',
    height: 60,
    flexDirection: 'row',
    paddingVertical: 10,
  },
  linkContainer: {
    width: '100%',
    justifyContent: 'center',
    flexDirection: 'row',
  },
  link: {
    color: 'blue',
  },
  infoContainer: {
    flexDirection: 'row',
    paddingVertical: 10,
  },
});

export default App;
