# Testing Integration with backend-automation-framework

## Setup Instructions

### 1. Push this repository to GitHub:

```bash
# Add your GitHub repository as remote
git remote add origin https://github.com/yourusername/user-management-system.git

# Push to GitHub
git push -u origin main
```

### 2. Verify your backend-automation-framework repository structure:

Your `backend-automation-framework` repository should have one of these structures:

**For Maven-based tests:**
```
backend-automation-framework/
├── pom.xml
├── src/
│   └── test/
│       └── java/
│           └── your test classes
└── README.md
```

**For Gradle-based tests:**
```
backend-automation-framework/
├── build.gradle
├── src/
│   └── test/
│       └── java/
│           └── your test classes
└── README.md
```

### 3. Test Configuration Requirements:

Your test framework should be able to accept these environment variables:
- `BASE_URL=http://localhost:8080` - The running application URL
- `TEST_USERNAME=admin` - Default admin username
- `TEST_PASSWORD=admin123` - Default admin password
- `DB_URL=jdbc:postgresql://localhost:5432/user_management_db_test` - Test database URL

### 4. Test the Automation:

1. **Create a test branch:**
```bash
git checkout -b feature/test-automation
```

2. **Make a small change (like updating README):**
```bash
echo "Testing PR automation" >> README.md
git add README.md
git commit -m "Test: Trigger automated testing"
```

3. **Push the branch:**
```bash
git push origin feature/test-automation
```

4. **Create a Pull Request** on GitHub from `feature/test-automation` to `master`

5. **Watch the GitHub Actions tab** - you should see the workflow running automatically!

## Expected Workflow Behavior:

1. **Environment Setup**: Creates PostgreSQL test database
2. **Application Build**: Compiles the User Management System
3. **Application Start**: Runs the application on port 8080
4. **Health Check**: Waits for the application to be ready
5. **Test Repository Clone**: Downloads your `backend-automation-framework`
6. **Test Execution**: Runs your tests against the live application
7. **Results Reporting**: Comments on the PR with success/failure status

## Troubleshooting:

### If the workflow fails to find your test repository:
- Ensure `backend-automation-framework` is public or accessible
- Check the repository name matches exactly
- Verify you have the correct permissions

### If tests fail to run:
- Check that your test repository has `pom.xml` or `build.gradle`
- Ensure your tests can run with the provided environment variables
- Verify your tests are configured to run against `http://localhost:8080`

### If the application fails to start:
- Check the workflow logs for PostgreSQL connection issues
- Ensure the application dependencies are correct

## Manual Testing (Local):

You can also test the integration locally:

1. **Start the database:**
```bash
docker-compose up -d postgres
```

2. **Start the application:**
```bash
mvn spring-boot:run
```

3. **In another terminal, clone and run your tests:**
```bash
cd ../
git clone https://github.com/yourusername/backend-automation-framework.git
cd backend-automation-framework
export BASE_URL=http://localhost:8080
mvn test  # or ./gradlew test
```

This will help you verify that your tests work correctly before pushing to GitHub.
