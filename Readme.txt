Android app, which leverages Support Vector Machines to predict your activity

(Please view About_app.pdf for more info)

OverviewThe objective of this project is to build an application which recognize human activities using machine learning technique. To recognize the activities, gathering datasets related to human actions in daily life would be required. Fortunately, we could solve this gathering issue using the smartphone. Nowadays, most people have their smartphone and the smartphones have many sensors such as accelerometer, gyroscope, orientation, GPS, proximity, etc. By using these sensors, we can obtain the datasets required for identifying human movements.

Part AIn this part, I generate the training database file to classify three different activities: walking, running, and eating. Each activity duration should be 5 seconds. The data sampling frequency should be over 10 Hz for the proper accuracy. Also, the number of each activity in the training dataset should be over 20. According to recent research papers, accelerometer sensor datasets (X, Y, and Z axes) are very useful for human activity recognition, so we suggest to use these accelerometer sensors as input dataset among many sensors in the smartphone.

Part BBased on the database which I generated (Part A), my application should classify the activities using Support Vector Machine. You can download free java SVM library from (http://www.csie.ntu.edu.tw/~cjlin/libsvm). You can download the android SVM application using the library (https://github.com/cnbuff410/Libsvm-androidjni). Also, it is fine to use other third party library or implement it yourself if you want. In your app, the SVM parameters and test accuracy should be displayed. The accuracy should be over 60%. For the validated test accuracy, we suggest to use ‘K- fold cross-validation technique’ which is supported by the library. The ‘K’ should be between three and five.

How the app works: 
The app contains 5 buttons: 

WALK 
RUN
EAT
DELETE TABLE
RECREATE TABLE
TRAIN
DO NOT CLICK THIS

WALK, RUN, EAT will be used to generate the dataset. Just click on any one of these 3 buttons, and perform the corresponding activity. The accelerometer will be activated for exactly 5 mins. The accelerometer data will be stored in the database with the table name "dairy". 

When the dataset has been built using the WALK, RUN, EAT buttons, click the TRAIN button. You can see the accuracy in console of the Android Studio. The dataset that I have provided gives accuracy of approx 69%.

The buttons DELETE TABLE and RECREATE TABLE will delete the table and make new table respectively.

DO NOT CLICK THIS has no functionality. So, please don't click this.

The Library has been downloaded from github created by a user named yctung. This is the link : https://github.com/yctung/AndroidLibSvm.
Plese make sure you have NDK installed.


