angular.module('starter.controllers', [])

.controller('DashCtrl', function($scope) {})

.controller('ChatsCtrl', function($scope, Chats) {
  // With the new view caching in Ionic, Controllers are only called
  // when they are recreated or on app start, instead of every page change.
  // To listen for when this page is active (for example, to refresh data),
  // listen for the $ionicView.enter event:
  //
  //$scope.$on('$ionicView.enter', function(e) {
  //});

  $scope.chats = Chats.all();
  $scope.remove = function(chat) {
    Chats.remove(chat);
  };
})

.controller('ChatDetailCtrl', function($scope, $stateParams, Chats) {
  $scope.chat = Chats.get($stateParams.chatId);
})

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
    $scope.time = $filter('date')(now, "dd/MM/yyyy HH:mm:ss");
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
