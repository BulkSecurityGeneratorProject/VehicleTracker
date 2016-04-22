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

.controller('AccountCtrl', function($scope, $ionicLoading) {
  $scope.settings = {
    enableGPS: false
  };

  $scope.position = {
    longitude: 0,
    latitude : 0
  };

  $scope.checkGPS = function() {
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
