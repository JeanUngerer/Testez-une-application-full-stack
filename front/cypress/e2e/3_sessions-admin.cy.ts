describe('admin sessions spec', () => {
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

  it('should edit the created session', () => {
    cy.get('[data-cy="session-card"]:first').should("exist")
      .get('[data-cy="edit-button"]:first').click();
    cy.url().should('include', '/sessions/update');

    cy.get('textarea[data-cy="description"]').type(" -- updated")
    cy.get('[data-cy="save-button"]').click();
    cy.url().should('include', '/sessions');
  });

  it('should delete the edited session', () => {
    cy.get('[data-cy="session-card"]:first').should("exist")
      .get('[data-cy="detail-button"]:first').click();
    cy.url().should('include', '/sessions/detail');

    cy.get('[data-cy="delete-session"]').click();
    cy.url().should('include', '/sessions');
  });
});
