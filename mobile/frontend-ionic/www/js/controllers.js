angular.module('starter.controllers', [])

.controller('DashCtrl', function($scope) {})

.controller('AccountCtrl', function($scope, $ionicLoading, $ENV, $timeout, $filter, $http) {

  $scope.host = $ENV.settings.API.host; //TODO ruins dynamic reload
  $scope.port = $ENV.settings.API.port;
  $scope.vehicle = "BMW123";
  $scope.driver = "Karl Meerkatz";
  $scope.order = "ORD000001";
  $scope.fromLatitude = "48.4105749";
  $scope.fromLongitude = "15.5977561";
  $scope.toLatitude = "48.2209603";
  $scope.toLongitude = "16.3815945";

  var id = window.localStorage.getItem('deviceId');
  if (!id) {
    id = Math.random().toString(36).slice(2);
    window.localStorage.setItem('deviceId', id);
  }
  $scope.deviceId = id;

  $scope.settings = {
    enableGPS: false
  };

  $scope.position = {
    longitude: 0,
    latitude : 0
  };

  updateTime();
  function updateTime () {
    var now = new Date().getTime();
    $scope.time = $filter('date')(now, "yyyy-MM-dd HH:mm:ss");
    $scope.deadline = $filter('date')(now, "yyyy-MM-dd HH:mm:ss");
    $timeout(updateTime, 1000);
  }

  $scope.checkGPS = function() {
    $scope.position.longitude = "";
    $scope.position.latitude = "";
    if ($scope.settings.enableGPS) {
      $ionicLoading.show({
        template: 'Checking GPS...'
      });
      navigator.geolocation.getCurrentPosition(function (position) {
        console.log(position.coords.latitude + ", " + position.coords.longitude);
        $scope.position.longitude = position.coords.longitude;
        $scope.position.latitude = position.coords.latitude;
        $ionicLoading.hide();
      });
    }  else {
      console.log("GPS disabled");
    }
  };

  $scope.checkGPS();

  //--- form defaults and actions ---//

  $scope.sendStammdaten = function() {
    var vehicle = getValue("vehicle");
    var driver = getValue("driver");
    console.log($scope.deviceId);
    console.log(vehicle);
    console.log(driver);
    post("stammdatens", "{\"deviceId\":\""+$scope.deviceId+"\", \"vehicle\":\""+vehicle+"\", \"driver\":\""+driver+"\"}")
  };

  $scope.sendOrder = function() {
    var order = getValue("order");
    var fromLatitude = getValue("fromLatitude");
    var fromLongitude = getValue("fromLongitude");
    var toLatitude = getValue("toLatitude");
    var toLongitude = getValue("toLongitude");
    var deadline = getValue("deadline");
    console.log($scope.deviceId);
    console.log(order);
    console.log(fromLatitude);
    console.log(fromLongitude);
    console.log(toLatitude);
    console.log(toLongitude);
    console.log(deadline);
    post("orders", "{\"deviceId\":\""+$scope.deviceId+"\", \"orderNr\":\""+order+"\", \"fromLatitude\":\""+fromLatitude+"\", \"fromLongitude\":\""+fromLongitude+"\", \"toLatitude\":\""+toLatitude+"\", \"toLongitude\":\""+toLongitude+"\", \"deadline\":\""+deadline+"\"}")
  };

  $scope.sendLocation = function() {
    console.log($scope.deviceId);
    console.log($scope.position.longitude);
    console.log($scope.position.latitude);
    console.log($scope.time);
    post("locations", "{\"deviceId\":\""+$scope.deviceId+"\", \"longitude\":\""+$scope.position.longitude+"\", \"latitude\":\""+$scope.position.latitude+"\", \"time\":\""+$scope.time+"\"}")
  };

  function getValue(field) {
    var value = document.getElementById(field).value;
    if (!value) value = $scope[field];
    return value;
  }

  function post(type, data) {

    // token
    $http.post('http://'+$scope.host+':'+$scope.port+"/api/authenticate", $ENV.settings.CREDENTIALS).then(function (successResult) {
      console.log(successResult);
      var token = successResult.data.id_token;
      $http.defaults.headers.common.Authorization = 'Bearer ' + token;

      // actual request:
      $http.post('http://'+$scope.host+':'+$scope.port+"/api/" + type, data).then(function (successResult) {
        console.log(successResult);
        alert("OK");
      }, function (errResult) {
        console.log(errResult);
        alert("FAILED");
      });

    }, function (errResult) {
      console.log(errResult);
      alert("FAILED");
    });
  }

});
