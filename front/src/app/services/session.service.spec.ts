import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log the user in and emit true', (done) => {
    // Arrange
    const sessionInformation: SessionInformation = {
      token: 'token_example',
      type: 'type_example',
      id: 1,
      username: 'username_example',
      firstName: 'firstName_example',
      lastName: 'lastName_example',
      admin: true
    };

    // Subscribe to observable
    service.$isLogged().subscribe((isLogged) => {
      // Assertion
      expect(isLogged).toBe(true);
      done();
    });

    // Act
    service.logIn(sessionInformation);
  });

  it('should log the user out and emit false', (done) => {
    // Arrange
    const sessionInformation: SessionInformation = {
      token: 'token_example',
      type: 'type_example',
      id: 1,
      username: 'username_example',
      firstName: 'firstName_example',
      lastName: 'lastName_example',
      admin: true
    };

    // Initial login
    service.logIn(sessionInformation);

    // Subscribe to observable
    service.$isLogged().subscribe((isLogged) => {
      // Assertion
      expect(isLogged).toBe(false);
      done();
    });

    // Act
    service.logOut();
  });
});
