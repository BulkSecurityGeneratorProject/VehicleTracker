'use strict';

describe('Controller Tests', function() {

    describe('Stammdaten Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStammdaten;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStammdaten = jasmine.createSpy('MockStammdaten');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Stammdaten': MockStammdaten
            };
            createController = function() {
                $injector.get('$controller')("StammdatenDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'vehicleTrackerApp:stammdatenUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
