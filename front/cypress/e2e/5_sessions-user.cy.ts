describe('user sessions spec', () => {
  it('should log as admin with 0 session registered', () => {
    cy.visit('/login')
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.get('[data-cy="sessions-link"]').click();

    cy.url().should('include', '/sessions');

    cy.get('[data-cy="available-title"]').should('exist');
    cy.get('[data-cy="session-card"]').should("not.exist");

  });

  it('should create a new session', () => {
    cy.get('[data-cy="create-button"]').click();

    cy.url().should('include', '/sessions/create');
    cy.get('input[data-cy="name"]').type("Première étoile")
    cy.get('input[data-cy="date"]').type("2024-04-01")
    cy.get('mat-select[data-cy="select"]').click()
    cy.get('mat-option[data-cy="option"]:first').click()
    cy.get('textarea[data-cy="description"]').type("descriptionnn")

    cy.get('[data-cy="save-button"]').click();
    cy.url().should('include', '/sessions');
  });

  it('should register a user', () => {
    cy.visit('http://localhost:4200/register')
    cy.get('input[formControlName="firstName"]').type('TestFirstName')
    cy.get('input[formControlName="lastName"]').type('TestLastName')
    cy.get('input[formControlName="email"]').type('testemail@test.com')
    cy.get('input[formControlName="password"]').type('testPassword')
    cy.get('[type="submit"]').should('be.enabled').click();
  })

  it('should log in successfully with a session available', () => {
    cy.visit('/login')
    cy.get('input[formControlName=email]').type("testemail@test.com")
    cy.get('input[formControlName=password]').type(`${"testPassword"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
    cy.get('[data-cy="session-card"]:first').should("exist")
  })

  it('should subscribe to the available session then unsubscribe', () => {
    cy.get('[data-cy="session-card"]:first').should("exist")
      .get('[data-cy="detail-button"]:first').click();
    cy.url().should('include', '/sessions/detail');

    cy.get('[data-cy="participate"]').click();
    cy.get('[data-cy="unparticipate"]').should("exist");

    cy.get('[data-cy="unparticipate"]').click();
    cy.get('[data-cy="participate"]').should("exist");
  });

  it('should delete user account', () => {
    cy.get('[data-cy="me"]').click();
    cy.url().should('include', '/me')
    cy.get('[data-cy="name"]').should('have.text', 'Name: TestFirstName TESTLASTNAME');
    cy.get('[data-cy="mail"]').should('have.text', 'Email: testemail@test.com');
    cy.get('[data-cy="delete-account"]').should('exist').click();
    cy.url().should('not.include', '/me');
  });

  it('should log as admin and delete the session', () => {
    cy.visit('/login')
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.get('[data-cy="sessions-link"]').click();

    cy.url().should('include', '/sessions');

    cy.get('[data-cy="session-card"]:first').should("exist")
      .get('[data-cy="detail-button"]:first').click();
    cy.url().should('include', '/sessions/detail');

    cy.get('[data-cy="delete-session"]').click();
    cy.get('[data-cy="session-card"]').should("not.exist");
  });
})
