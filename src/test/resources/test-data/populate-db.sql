DELETE FROM users;

INSERT INTO users (id, auth_subject, notification_enabled, notification_email, notification_time, time_zone, language) VALUES
    ('018f1c9a-1111-4e7b-a9e1-12a7f6f01a10', 'auth0|user_001', TRUE, 'alice@example.com', '00:00', 'America/New_York', 'en'),
    ('018f1c9a-1112-4b6d-bc42-87f5c6b20a21', 'auth0|user_002', TRUE, 'bob@example.com', '10:45', 'Asia/Kathmandu', 'en'),
    ('018f1c9a-1113-4c82-91f5-21c0e9d54a32', 'auth0|user_003', TRUE, 'carol@example.com', '14:00', 'Asia/Tokyo', 'ja'),
    ('018f1c9a-1114-4a0d-8d2f-09c36e8e5a43', 'auth0|user_004', FALSE, 'dave@example.com', '06:00', 'Europe/Berlin', 'de'),
    ('018f1c9a-1115-476f-a6e8-55d2a3b65a54', 'auth0|user_005', TRUE, 'eve@example.com', '06:30', 'America/Los_Angeles','es');

INSERT INTO curriculums (id, user_id, queue_position, topic, current_unit_number) VALUES
    -- Alice
    ('018f1d00-2001-4f3a-9e11-aaa111111001', '018f1c9a-1111-4e7b-a9e1-12a7f6f01a10', 0, 'Intro to Databases', 1),
    ('018f1d00-2002-4f3a-9e11-aaa111111002', '018f1c9a-1111-4e7b-a9e1-12a7f6f01a10', 1, 'SQL Basics', 1),
    ('018f1d00-2003-4f3a-9e11-aaa111111003', '018f1c9a-1111-4e7b-a9e1-12a7f6f01a10', -1, 'Advanced Queries', 3),

    -- Bob
    ('018f1d00-2004-4f3a-9e11-aaa111111004', '018f1c9a-1112-4b6d-bc42-87f5c6b20a21', 0, 'Web Development', 1),
    ('018f1d00-2005-4f3a-9e11-aaa111111005', '018f1c9a-1112-4b6d-bc42-87f5c6b20a21', 1, 'JavaScript Fundamentals', 1),
    ('018f1d00-2006-4f3a-9e11-aaa111111006', '018f1c9a-1112-4b6d-bc42-87f5c6b20a21', -1, 'Frontend Frameworks', 3),

    -- Carol
    ('018f1d00-2007-4f3a-9e11-aaa111111007', '018f1c9a-1113-4c82-91f5-21c0e9d54a32', -1, 'Data Science', 3),
    ('018f1d00-2008-4f3a-9e11-aaa111111008', '018f1c9a-1113-4c82-91f5-21c0e9d54a32', -1, 'Python for Analysis', 3),
    ('018f1d00-2009-4f3a-9e11-aaa111111009', '018f1c9a-1113-4c82-91f5-21c0e9d54a32', -1, 'Machine Learning', 3),

    -- Dave
    ('018f1d00-2010-4f3a-9e11-aaa111111010', '018f1c9a-1114-4a0d-8d2f-09c36e8e5a43', 0, 'Networking Basics', 1),
    ('018f1d00-2011-4f3a-9e11-aaa111111011', '018f1c9a-1114-4a0d-8d2f-09c36e8e5a43', 1, 'Cybersecurity Fundamentals', 1),
    ('018f1d00-2012-4f3a-9e11-aaa111111012', '018f1c9a-1114-4a0d-8d2f-09c36e8e5a43', -1, 'Ethical Hacking', 3),

    -- Eve
    ('018f1d00-2013-4f3a-9e11-aaa111111013', '018f1c9a-1115-476f-a6e8-55d2a3b65a54', 0, 'AI Foundations', 1),
    ('018f1d00-2014-4f3a-9e11-aaa111111014', '018f1c9a-1115-476f-a6e8-55d2a3b65a54', 1, 'Deep Learning', 1),
    ('018f1d00-2015-4f3a-9e11-aaa111111015', '018f1c9a-1115-476f-a6e8-55d2a3b65a54', -1, 'AI Ethics', 3);

INSERT INTO learning_units (id, curriculum_id, number, heading, subheading, content) VALUES
    -- Alice -----------------------------------------------------------------------------------------------------------
    -- Intro to Databases
    ('018f1e00-3001-4a9e-9e11-bbb111111001', '018f1d00-2001-4f3a-9e11-aaa111111001', 1, 'What is a Database?', 'Understanding structured data storage', 'A database is an organized collection of data that can be easily accessed, managed, and updated.'),
    ('018f1e00-3002-4a9e-9e11-bbb111111002', '018f1d00-2001-4f3a-9e11-aaa111111001', 2, 'Database Models', 'Relational vs Non-relational', 'Covers differences between relational databases and newer NoSQL models.'),
    ('018f1e00-3003-4a9e-9e11-bbb111111003', '018f1d00-2001-4f3a-9e11-aaa111111001', 3, 'DBMS Overview', 'How database management systems work', 'Explains how DBMSs provide interfaces for defining, querying, and managing data.'),

    -- SQL Basics
    ('018f1e00-3004-4a9e-9e11-bbb111111004', '018f1d00-2002-4f3a-9e11-aaa111111002', 1, 'Introduction to SQL', 'Core language concepts', 'SQL stands for Structured Query Language and is used to communicate with databases.'),
    ('018f1e00-3005-4a9e-9e11-bbb111111005', '018f1d00-2002-4f3a-9e11-aaa111111002', 2, 'SELECT Statements', 'Querying data', 'The SELECT statement is used to retrieve data from one or more tables.'),
    ('018f1e00-3006-4a9e-9e11-bbb111111006', '018f1d00-2002-4f3a-9e11-aaa111111002', 3, 'Filtering Data', 'Using WHERE clauses', 'The WHERE clause allows filtering rows based on specific conditions.'),

    -- Advanced Queries
    ('018f1e00-3007-4a9e-9e11-bbb111111007', '018f1d00-2003-4f3a-9e11-aaa111111003', 1, 'JOIN Operations', 'Combining multiple tables', 'Learn how INNER JOIN, LEFT JOIN, and RIGHT JOIN work in SQL.'),
    ('018f1e00-3008-4a9e-9e11-bbb111111008', '018f1d00-2003-4f3a-9e11-aaa111111003', 2, 'Subqueries', 'Queries within queries', 'A subquery is a nested SQL query used to further filter or calculate data.'),
    ('018f1e00-3009-4a9e-9e11-bbb111111009', '018f1d00-2003-4f3a-9e11-aaa111111003', 3, 'Indexes & Optimization', 'Improving query performance', 'Indexes help speed up data retrieval but may slow down insert operations.'),

    -- Bob -------------------------------------------------------------------------------------------------------------
    -- Web Development
    ('018f1e00-4001-4a9e-9e11-bbb111111001', '018f1d00-2004-4f3a-9e11-aaa111111004', 1, 'Introduction to Web Development', 'Understanding the web ecosystem', 'Overview of how websites work, from clients to servers.'),
    ('018f1e00-4002-4a9e-9e11-bbb111111002', '018f1d00-2004-4f3a-9e11-aaa111111004', 2, 'HTML & CSS Basics', 'Building web pages', 'Learn to structure content with HTML and style it using CSS.'),
    ('018f1e00-4003-4a9e-9e11-bbb111111003', '018f1d00-2004-4f3a-9e11-aaa111111004', 3, 'Responsive Design', 'Making pages mobile-friendly', 'Understand how to use flexbox and media queries to create responsive layouts.'),

    -- JavaScript Fundamentals
    ('018f1e00-4004-4a9e-9e11-bbb111111004', '018f1d00-2005-4f3a-9e11-aaa111111005', 1, 'Intro to JavaScript', 'Core syntax and concepts', 'JavaScript adds interactivity and logic to web pages.'),
    ('018f1e00-4005-4a9e-9e11-bbb111111005', '018f1d00-2005-4f3a-9e11-aaa111111005', 2, 'Variables & Functions', 'Writing reusable code', 'Learn how to declare variables and write reusable functions.'),
    ('018f1e00-4006-4a9e-9e11-bbb111111006', '018f1d00-2005-4f3a-9e11-aaa111111005', 3, 'DOM Manipulation', 'Interacting with web pages', 'Use JavaScript to dynamically modify and respond to page elements.'),

    -- Frontend Frameworks
    ('018f1e00-4007-4a9e-9e11-bbb111111007', '018f1d00-2006-4f3a-9e11-aaa111111006', 1, 'Framework Overview', 'Why use frameworks?', 'Introduces popular frontend frameworks like React, Vue, and Angular.'),
    ('018f1e00-4008-4a9e-9e11-bbb111111008', '018f1d00-2006-4f3a-9e11-aaa111111006', 2, 'Component-Based Design', 'Reusable UI building blocks', 'Learn how to create modular components that can be reused across projects.'),
    ('018f1e00-4009-4a9e-9e11-bbb111111009', '018f1d00-2006-4f3a-9e11-aaa111111006', 3, 'State Management', 'Handling dynamic data', 'Covers the concept of state and how to manage it effectively in large applications.'),

    -- Carol -----------------------------------------------------------------------------------------------------------
    -- Data Science
    ('018f1e00-5001-4a9e-9e11-bbb111111001', '018f1d00-2007-4f3a-9e11-aaa111111007', 1, 'Introduction to Data Science', 'Overview and applications', 'Data Science involves extracting insights from structured and unstructured data.'),
    ('018f1e00-5002-4a9e-9e11-bbb111111002', '018f1d00-2007-4f3a-9e11-aaa111111007', 2, 'Data Collection', 'Sources and methods', 'Learn about APIs, web scraping, and datasets for data science projects.'),
    ('018f1e00-5003-4a9e-9e11-bbb111111003', '018f1d00-2007-4f3a-9e11-aaa111111007', 3, 'Data Cleaning', 'Preparing data for analysis', 'Covers handling missing values, outliers, and inconsistent data.'),

    -- Python for Analysis
    ('018f1e00-5004-4a9e-9e11-bbb111111004', '018f1d00-2008-4f3a-9e11-aaa111111008', 1, 'NumPy Basics', 'Working with arrays', 'Learn how to use NumPy for numerical computing and array manipulation.'),
    ('018f1e00-5005-4a9e-9e11-bbb111111005', '018f1d00-2008-4f3a-9e11-aaa111111008', 2, 'Pandas for Data Analysis', 'Handling tabular data', 'Pandas provides powerful tools for cleaning, transforming, and analyzing data.'),
    ('018f1e00-5006-4a9e-9e11-bbb111111006', '018f1d00-2008-4f3a-9e11-aaa111111008', 3, 'Visualization with Matplotlib', 'Plotting and charts', 'Create informative visualizations to explore and present your findings.'),

    -- Machine Learning
    ('018f1e00-5007-4a9e-9e11-bbb111111007', '018f1d00-2009-4f3a-9e11-aaa111111009', 1, 'Machine Learning Overview', 'Supervised vs unsupervised', 'Covers ML concepts, data splitting, and model training basics.'),
    ('018f1e00-5008-4a9e-9e11-bbb111111008', '018f1d00-2009-4f3a-9e11-aaa111111009', 2, 'Regression Models', 'Predicting continuous values', 'Learn how to build and evaluate linear and polynomial regression models.'),
    ('018f1e00-5009-4a9e-9e11-bbb111111009', '018f1d00-2009-4f3a-9e11-aaa111111009', 3, 'Classification Models', 'Predicting categories', 'Understand logistic regression, decision trees, and evaluation metrics.'),

    -- Dave -----------------------------------------------------------------------------------------------------------
    -- Networking Basics
    ('018f1e00-6001-4a9e-9e11-bbb111111001', '018f1d00-2010-4f3a-9e11-aaa111111010', 1, 'Introduction to Networking', 'Understanding computer networks', 'Covers the fundamentals of how computers communicate over networks.'),
    ('018f1e00-6002-4a9e-9e11-bbb111111002', '018f1d00-2010-4f3a-9e11-aaa111111010', 2, 'OSI Model', 'Seven layers of networking', 'Explains the OSI model and the roles of each layer in communication.'),
    ('018f1e00-6003-4a9e-9e11-bbb111111003', '018f1d00-2010-4f3a-9e11-aaa111111010', 3, 'IP Addressing', 'IPv4 and IPv6 basics', 'Describes how IP addressing works and how devices identify each other.'),

    -- Cybersecurity Fundamentals
    ('018f1e00-6004-4a9e-9e11-bbb111111004', '018f1d00-2011-4f3a-9e11-aaa111111011', 1, 'Introduction to Cybersecurity', 'Protecting digital assets', 'Overview of security principles and the importance of data protection.'),
    ('018f1e00-6005-4a9e-9e11-bbb111111005', '018f1d00-2011-4f3a-9e11-aaa111111011', 2, 'Common Threats', 'Understanding vulnerabilities', 'Discusses malware, phishing, and social engineering attacks.'),
    ('018f1e00-6006-4a9e-9e11-bbb111111006', '018f1d00-2011-4f3a-9e11-aaa111111011', 3, 'Security Best Practices', 'Defensive strategies', 'Covers password hygiene, encryption, and network defense basics.'),

    -- Ethical Hacking
    ('018f1e00-6007-4a9e-9e11-bbb111111007', '018f1d00-2012-4f3a-9e11-aaa111111012', 1, 'Ethical Hacking Overview', 'Legal and ethical boundaries', 'Introduces ethical hacking principles and legal considerations.'),
    ('018f1e00-6008-4a9e-9e11-bbb111111008', '018f1d00-2012-4f3a-9e11-aaa111111012', 2, 'Penetration Testing', 'Simulating attacks', 'Explains how ethical hackers test systems for vulnerabilities.'),
    ('018f1e00-6009-4a9e-9e11-bbb111111009', '018f1d00-2012-4f3a-9e11-aaa111111012', 3, 'Reporting and Mitigation', 'Improving security posture', 'Learn how to document findings and recommend remediation steps.'),

    -- Eve -------------------------------------------------------------------------------------------------------------
    -- AI Foundations
    ('018f1e00-7001-4a9e-9e11-bbb111111001', '018f1d00-2013-4f3a-9e11-aaa111111013', 1, 'Introduction to AI', 'History and concepts', 'AI is the simulation of human intelligence in machines to perform tasks such as learning and reasoning.'),
    ('018f1e00-7002-4a9e-9e11-bbb111111002', '018f1d00-2013-4f3a-9e11-aaa111111013', 2, 'Types of AI', 'Narrow vs General AI', 'Explains the difference between task-specific AI and general-purpose intelligence.'),
    ('018f1e00-7003-4a9e-9e11-bbb111111003', '018f1d00-2013-4f3a-9e11-aaa111111013', 3, 'Applications of AI', 'Real-world use cases', 'Covers how AI is used in healthcare, finance, transportation, and more.'),

    -- Deep Learning
    ('018f1e00-7004-4a9e-9e11-bbb111111004', '018f1d00-2014-4f3a-9e11-aaa111111014', 1, 'Neural Networks Basics', 'Understanding neurons and layers', 'Introduces artificial neural networks and how they mimic the human brain.'),
    ('018f1e00-7005-4a9e-9e11-bbb111111005', '018f1d00-2014-4f3a-9e11-aaa111111014', 2, 'Training Deep Models', 'Backpropagation and optimization', 'Explains how neural networks learn through backpropagation and gradient descent.'),
    ('018f1e00-7006-4a9e-9e11-bbb111111006', '018f1d00-2014-4f3a-9e11-aaa111111014', 3, 'Convolutional Networks', 'Image recognition basics', 'Covers CNNs and their role in computer vision applications.'),

    -- AI Ethics
    ('018f1e00-7007-4a9e-9e11-bbb111111007', '018f1d00-2015-4f3a-9e11-aaa111111015', 1, 'Ethical Considerations', 'Bias and fairness in AI', 'Discusses the social impact of biased data and algorithmic fairness.'),
    ('018f1e00-7008-4a9e-9e11-bbb111111008', '018f1d00-2015-4f3a-9e11-aaa111111015', 2, 'Privacy and Accountability', 'Responsible AI development', 'Explains why transparency and data privacy are vital in AI systems.'),
    ('018f1e00-7009-4a9e-9e11-bbb111111009', '018f1d00-2015-4f3a-9e11-aaa111111015', 3, 'Future of AI Ethics', 'Regulations and human oversight', 'Explores emerging AI laws, ethical frameworks, and the need for human governance.');
