#Air Lift Key Services (ALKS)

##Summary: 

Filling the gap for key services between what Amazon provides and the need for system admins/users to create manage temporary tokens to access AWS resources.
The purpose of the tool is to automate generation of federated tokens on demand. It makes it easy for system admin's to manage, create and track issuance of these tokens. User validation is performed against LDAP and the application checks if user belongs to any of the LDAP groups before displaying respective options to generate keys.

##Features

###Generate session tokens
* 	Create temporary tokens to AWS resources for a particular duration with click of a button.
* 	Access is restricted through AD group assignment

###Authentication and Authorization
*	Use internal LDAP for Authentication and Authorization
*	VPC/VPN Connection to AWS account and on premise LDAP directory 
*	Control user access through LDAP directory services
*	Admin role with elevated privileges
*	User is limited to create tokens using one of the roles which are based on the LDAP group they belong to.

###Auditing/Logging
* 	Track issuance of tokens and history through logs within ALKS

###Security
*	Critical account data is encrypted in DynamoDB using client side encryption

###Design Constraints
AWS SDK has a constrained policy length (2048 bytes) to generate tokens. 
Refer to the following document: http://docs.aws.amazon.com/STS/latest/APIReference/API_GetFederationToken.html

###Dependencies
1. Dependency versions
    - Tomcat 7.0 server
    - Maven as build script
    - Java 1.8
    - AWS SDK  1.10.2 https://aws.amazon.com/releasenotes/6075454994675787
    - Refer to pom.xml for other dependencies. 
2. Other dependencies:
	- Client side encryption for DynamoDB
	- Project is compiled and jar is placed in WEB-INF/lib folder.  You will need to create a key store file. Please refer to the following blog for more details on how to use. https://java.awsblog.com/post/TxI32GE4IG2SNS/Client-side-Encryption-for-Amazon-DynamoDB

###Installation and Setup
1. Get the project from git and setup as a Dynamic Web project. Make changes to the build path as needed. 
2. Update the properties file with all the configuration values and customized messages. Refer to properties file descriptions for additional details.
3. Create a keystore file (alks.jck) to store your encryption keys for encrypting long term keys that will be stored in DynamoDB. You should save this file in src folder (or in the classpath).
4. Authentication: This application uses core javax.naming.* package to validate users against LDAP. To identify an admin user, add a new AD group and update the messages.properties file. User should be part of the group to be recognized as an admin.
5. DynamoDB: Create three tables with following columns 
    - accounts(accountNo -> HashKey)
    - accountrolepolicy(id -> HashKey)
    - accountidgroup(accountrolepolicy_id -> HashKey and adGroup -> RangeKey) 
      Use java file (ALKSDynamoDBSchema.java) to create these tables.

###AWS Services/APIs Utilized
1. AWS IAM Services
    - Get account details
    - List roles for an account
    - Generate federated tokens.
2. AWS DynamoDB 

###Application Functionality
1. Login Screen: Users can login using their network credentials (UPN/password) to login. 
After successful login, user will be displayed with list of accounts and roles for which keys can be generated against.
2. If the logged in user belongs to an Admin LDAP group, the user is identified as admin user and will have additional menu options to 
    - View and Add/Update and inactivate accounts
    - View and Add/Update ARP relation
    - View and Add/Update ADG relation
3. Accounts Screen: Accounts screen displays list of accounts that have been added. For security reasons secret key is not displayed. The ‘Inactivate’ button can be used to inactivate an account. Once account is inactivated, it will not be shown on any other screens either to generate keys nor to create new ARP’s or AdGroup relationship records. 
    - Get account details
    - List roles for an account
    - Generate federated tokens.
2. AWS DynamoDB 

###Application Functionality
- Login Screen: Users can login using their network credentials (UPN/password) to login. 
After successful login, user will be displayed with list of accounts and roles for which keys can be generated against.

Login Screen

![Screen Shot Login ](/images/login.png)

Home Screen

![Screen Shot Home ](/images/home.png)

- If the logged in user belongs to an Admin LDAP group, the user is identified as admin user and will have additional menu options to 
  - View and Add/Update and inactivate accounts
  - View and Add/Update ARP relation
  - View and Add/Update ADG relation

- Accounts Screen: Accounts screen displays list of accounts that have been added. For security reasons secret key is not displayed. The ‘Inactivate’ button can be used to inactivate an account. Once account is inactivated, it will not be shown on any other screens either to generate keys nor to create new ARP’s or AdGroup relationship records. 

Accounts Screen

![Screen Shot Accounts ](/images/accounts.png)

- Admin user has option to Add/Update additional accounts. New account can be added by long term keys. Long term keys if correct, gets account number and account alias. Account number is a read-only field and alias can be changed if needed. Secret Key will be encrypted and stored.

Add Account Screen

![Screen Shot Add Account ](/images/add_account.png)

Note: In addition to having elevated privileges, these long term keys will need to have IAM read only access role, this is needed to fetch 

  - All the roles for account.
  - Account number and alias 

- ARP(Account Role Policy) relation screen: This screen displays existing list of accounts AWS Role and policy relationships. This screen will be used to customize access rights to each AWS Role.

Account Role Policy (ARP) relation Screen

![Screen Shot ARP ](/images/arp.png)

- Admin user will have option to add new ARP record. User selects the account and AWS role and inputs the policy. The policy that is entered for the selected role will give respective access to the generated tokens. So within the application this policy will redefine access rights to the role selected but does not make any changes in AWS account. 

Add ARP Screen

![Screen Shot Add ARP ](/images/add_arp.png)

- ADG(LDAP/Active Directory Group) relation screen: This screen will display all the relationships between the ARP (account role policy) and LDAP groups.  If logged in user belongs to any of these LDAP groups that been mapped, they will have an option on the home screen to generate federated tokens in those respective account/role combination. 

AD Group relation screen

![Screen Shot AD Group ](/images/adg.png)

- Admin Users can add new LDAP Group relationship by selecting a pre-populated accounts and respective ARP records created earlier. All LDAP groups are displayed to select from

Add AD Group relation screen

![Screen Shot AD Group Relation ](/images/add_adg.png)

###References 

- http://docs.aws.amazon.com/STS/latest/APIReference/API_GetFederationToken.html
- https://java.awsblog.com/post/TxI32GE4IG2SNS/Client-side-Encryption-for-Amazon-DynamoDB

