angular.module('starter.controllers', [])

.controller('DashCtrl', function($scope) {})

.controller('AccountCtrl', function($scope, $ionicLoading, $ENV, $timeout, $filter) {

  $scope.host = $ENV.settings.API.host;
  $scope.port = $ENV.settings.API.port;

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

});
