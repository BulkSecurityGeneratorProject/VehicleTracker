(function() {
    'use strict';

    angular
        .module('vehicleTrackerApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$scope', '$rootScope', '$state', 'Auth', 'Principal', 'ENV', 'LoginService'];

    function NavbarController ($scope, $rootScope, $state, Auth, Principal, ENV, LoginService) {
        var vm = this;

        vm.navCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.inProduction = ENV === 'prod';
        vm.login = login;
        vm.logout = logout;
        vm.$state = $state;
        vm.account = null;
        $rootScope.loggedInUser = null;

        function login () {
            LoginService.open();
        }

        function logout () {
            Auth.logout();
            vm.account = null;
            $rootScope.loggedInUser = null;
            $state.go('home');
        }

        getAccount();
        $scope.$on('authenticationSuccess', function(){
            vm.account = getAccount();
        });

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                $rootScope.loggedInUser = account.login;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
    }
})();
