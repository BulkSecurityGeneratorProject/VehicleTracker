angular.module('starter.controllers', [])

.controller('DashCtrl', function($scope) {})

.controller('AccountCtrl', function($scope, $ionicLoading, $ENV, $timeout, $filter) {

  $scope.host = $ENV.settings.API.host;
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
  };

  $scope.sendLocation = function() {
    console.log($scope.deviceId);
    console.log($scope.position.longitude);
    console.log($scope.position.latitude);
    console.log($scope.time);
  };

  function getValue(field) {
    var value = document.getElementById(field).value;
    if (!value) value = $scope[field];
    return value;
  }

});
