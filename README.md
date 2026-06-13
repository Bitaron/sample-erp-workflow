# Sample ERP Workflow 

A small ERP-style workflow management system demonstrating:

-   Business workflow design
-   Department-based approval flow
-   Role-based access control
-   Workflow audit tracking
-   Billing request to invoice generation process

## How to Run

### Prerequisites

-   Docker
-   Docker Compose

### Run

From the project root:

``` bash
./run.sh
```

This will build and start:

-   React frontend
-   Spring Boot backend

Application URLs:

Frontend:

    http://localhost:5173

Backend:

    http://localhost:8080

Stop:

``` bash
docker compose down
```

# Review Credentials

## Admin

Username:

    admin

Password:

    admin123

Capabilities:

-   Manage users
-   Manage departments
-   Create workflows

## Sales User

Username:

    sale

Password:

    sale

Capabilities:

-   Create billing requests
-   Submit requests

## Accounts User

Username:

    acc

Password:

    acc

Capabilities:

-   Review billing requests
-   Approve requests
-   View audit history

# Demo Flow

1.  Login as admin.
2.  Create workflow:

```{=html}
<!-- -->
```
    Sales -> Accounts

3.  Login as sales user.
4.  Create billing request.
5.  Login as accounts user.
6.  Approve request.
7.  System generates invoice and audit history.

# Architecture

## Backend

Technology:

-   Java 21
-   Spring Boot
-   Spring Security JWT
-   Spring Data JPA
-   H2 Database

Architecture:

    Controller
        |
    Service
        |
    Repository
        |
    Database

Feature-based packages:

    userManagement
    workflowManagement
    billingManagement
    security
    common

## Frontend

Technology:

-   React
-   React Router
-   Axios
-   React Query
-   Material UI

The frontend consumes APIs and does not contain business workflow rules.

# Design Notes

The solution separates:

## Workflow Definition

Defines reusable approval flow.

Example:

    Billing Approval Workflow

    Step 1: Sales
    Step 2: Accounts

## Workflow Instance

Represents an executing business process.

Example:

    Billing Request #100

    Status: IN_PROGRESS
    Current Step: Accounts Approval

# Tradeoffs

## Generic Workflow Model

Advantage:

-   Reusable for different document types.

Tradeoff:

-   More entities and complexity compared to a fixed workflow.

## Sequential Approval

Current implementation supports:

    Department A -> Department B

Not included:

-   Parallel approval
-   Conditional routing
-   Approval limits

## H2 Database

Used for self-contained deployment.

Production systems should use:

-   PostgreSQL
-   MySQL
-   Oracle

with migration tooling.

# Known Issues

-   No support to select different workflow.
-   No workflow versioning.
-   No file attachment support.
-   No email or push notifications.
-   Limited reporting.
-   Sequential workflow only.
-   Multiple workflow for same document type not supported. 

# Future Improvements

-   Conditional workflows
-   Parallel approvals
-   Approval delegation
-   Notifications
-   SLA monitoring
-   Workflow analytics
-   PostgreSQL migration
-   CI/CD pipeline
-   Production monitoring

# Assumptions

1.  Documents use one sequential approval workflow.
2.  Department users approve assigned workflow steps.
3.  Invoice is generated after final approval.
4.  The implementation focuses on architecture and maintainability
    rather than full ERP scope.
