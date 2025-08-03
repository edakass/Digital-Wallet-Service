# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).


## 2025-08-03

### Added
- `application.properties`, `controller`, `dto`, `repository`, `security`, `service` packages and initial `README`
- Unit tests for  `WalletServiceImpl` and `TransactionServiceImpl`


## 2025-08-02

### Added
- `init.sql` file for initializing database schema
- `enums` package with: `Currency`, `TransactionType`, `TransactionStatus`, `Role`, `OppositePartyType`
- `model` package: `Customer`, `Employee`, `Transaction`, `Wallet`
- `exception` package with custom exceptions (e.g., `CustomerNotFoundException`, `WalletAlreadyExistsException`, etc.)

### Changed
- Updated `pom.xml`


## 2025-07-31

### Added
- Initial project setup
- GitHub integration