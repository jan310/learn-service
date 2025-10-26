-- RESET TABLES
DELETE FROM users;

-- =====================================================
-- USERS
-- =====================================================
INSERT INTO users (id, auth_subject, notification_enabled, notification_email, notification_time, time_zone, language)
VALUES
    ('b18a2b8c-4a14-4b1a-9e4b-44ef437f1b9a', 'auth0|user_001', TRUE,  'alice@example.com', '08:00', 'America/New_York', 'en'),
    ('a91e1b12-023d-45c9-8e53-828b10da1b77', 'auth0|user_002', TRUE,  'bob@example.com', '09:30', 'Europe/London', 'en'),
    ('c63a90d1-6cc2-48fd-b889-1d3cb50b2b56', 'auth0|user_003', FALSE, 'carol@example.com', '07:15', 'Asia/Tokyo', 'ja'),
    ('d2b7c730-77e9-4f9a-8b4f-1b53ff45b182', 'auth0|user_004', TRUE,  'david@example.com', '10:00', 'Europe/Berlin', 'de'),
    ('e315a5a3-7dc7-4d9a-9f28-3b514e6e08c1', 'auth0|user_005', TRUE,  'emma@example.com', '14:45', 'Asia/Kathmandu', 'en');

-- =====================================================
-- CURRICULUMS
-- =====================================================
INSERT INTO curriculums (id, user_id, status, topic, current_unit_number)
VALUES
    -- Alice (2 curriculums)
    ('a1f5033a-4b4d-47cd-90ce-d0b3df73af21', 'b18a2b8c-4a14-4b1a-9e4b-44ef437f1b9a', 'ACTIVE', 'Introduction to AI', 2),
    ('b1e512ad-f20e-4c6c-b1ac-0cdb3126fa49', 'b18a2b8c-4a14-4b1a-9e4b-44ef437f1b9a', 'FINISHED', 'Data Science Basics', 5),

    -- Bob (3 curriculums)
    ('b21b8e5e-733d-41d8-9062-8900563f2d39', 'a91e1b12-023d-45c9-8e53-828b10da1b77', 'ACTIVE', 'Web Development', 1),
    ('b93c4a67-2196-4977-9c83-f8d8571cc978', 'a91e1b12-023d-45c9-8e53-828b10da1b77', 'FINISHED', 'UI/UX Design', 3),
    ('b49d8d4f-3eab-4efb-80ad-f19d1bca5e1b', 'a91e1b12-023d-45c9-8e53-828b10da1b77', 'FINISHED', 'Frontend Frameworks', 4),

    -- Carol (1 curriculum)
    ('c0b2a776-5b19-4b14-8e8e-705708c8e2b9', 'c63a90d1-6cc2-48fd-b889-1d3cb50b2b56', 'FINISHED', 'Machine Learning', 5),
    ('c3e2f890-4f3d-4b29-8a7e-5b21d9d2c47f', 'c63a90d1-6cc2-48fd-b889-1d3cb50b2b56', 'ACTIVE', 'Java', 1),

    -- David (3 curriculums)
    ('d02c9832-8b3e-4cc7-84a1-5ebeb9c13b12', 'd2b7c730-77e9-4f9a-8b4f-1b53ff45b182', 'ACTIVE', 'Cloud Computing', 2),
    ('d4acaf91-22df-4206-a373-15de787d48e4', 'd2b7c730-77e9-4f9a-8b4f-1b53ff45b182', 'FINISHED', 'Networking Fundamentals', 3),
    ('d8e1231f-1a56-4739-a9aa-2a68cda3e4b7', 'd2b7c730-77e9-4f9a-8b4f-1b53ff45b182', 'FINISHED', 'DevOps Essentials', 3),

    -- Emma (2 curriculums)
    ('e08da6a4-6bce-4a0f-b0cc-b52b8ab39b89', 'e315a5a3-7dc7-4d9a-9f28-3b514e6e08c1', 'ACTIVE', 'Cybersecurity Basics', 3),
    ('e5e8cc3e-22e3-4b63-802e-92a6ceab5b8b', 'e315a5a3-7dc7-4d9a-9f28-3b514e6e08c1', 'FINISHED', 'Cloud Infrastructure', 4);

-- =====================================================
-- LEARNING UNITS
-- =====================================================
INSERT INTO learning_units (id, curriculum_id, number, heading, subheading, content)
VALUES
-- Alice: Introduction to AI (4 units)
('f101c216-726d-46c3-97b1-693479c1ff01','a1f5033a-4b4d-47cd-90ce-d0b3df73af21',1,'What is AI?','Definition','An introduction to artificial intelligence.'),
('f202c317-827e-47d4-a8c2-793580d2ff02','a1f5033a-4b4d-47cd-90ce-d0b3df73af21',2,'History of AI','Early milestones','Key events in AI development.'),
('f303c418-928f-48e5-b9d3-893681e3ff03','a1f5033a-4b4d-47cd-90ce-d0b3df73af21',3,'AI Applications','Practical uses',null),
('f404c519-a29a-49f6-ae4d-993782f4ff04','a1f5033a-4b4d-47cd-90ce-d0b3df73af21',4,'Future of AI','Trends',null),

-- Alice: Data Science Basics (5 units)
('f505c620-b3ab-4af7-bbf5-a94783f5ff05','b1e512ad-f20e-4c6c-b1ac-0cdb3126fa49',1,'Introduction','What is Data Science?','Overview of the field.'),
('f606c721-c4bc-4bf8-ecf6-b95784f6ff06','b1e512ad-f20e-4c6c-b1ac-0cdb3126fa49',2,'Data Collection','Sources','How to collect quality data.'),
('f707c822-d5cd-4cf9-fdf7-ba6785f7ff07','b1e512ad-f20e-4c6c-b1ac-0cdb3126fa49',3,'Data Cleaning','Preparation','How to clean and preprocess data.'),
('f808c923-e6de-4dfa-aef8-bb7896f8ff08','b1e512ad-f20e-4c6c-b1ac-0cdb3126fa49',4,'Exploration','Visualization','Using graphs to understand data.'),
('f909da24-f7ef-4e0b-bff9-bc8907f9ff09','b1e512ad-f20e-4c6c-b1ac-0cdb3126fa49',5,'Modeling','Machine Learning Intro','Intro to simple models.'),

-- Bob: Web Development (3 units)
('fa10db25-0801-4f1c-a001-bd9018f0ff10','b21b8e5e-733d-41d8-9062-8900563f2d39',1,'HTML Basics','Syntax','Intro to HTML tags.'),
('fa21dc26-1902-4f2d-b112-be0129f1ff11','b21b8e5e-733d-41d8-9062-8900563f2d39',2,'CSS Basics','Styling',null),
('fa32dd27-2a03-4f3e-b223-bf123af2ff12','b21b8e5e-733d-41d8-9062-8900563f2d39',3,'JavaScript Basics','Logic',null),

-- Bob: UI/UX Design (3 units)
('9d1a2b34-5c6d-4e7f-8123-0a1b2c3d4e5f','b93c4a67-2196-4977-9c83-f8d8571cc978',1,'Design Principles','Fundamentals','Core principles of visual and interaction design.'),
('2a3b4c5d-6e7f-4801-9234-5a6b7c8d9e0f','b93c4a67-2196-4977-9c83-f8d8571cc978',2,'User Research','Methods','Techniques for interviewing and usability testing.'),
('3b4c5d6e-7f80-4912-0345-6b7c8d9e0f1a','b93c4a67-2196-4977-9c83-f8d8571cc978',3,'Wireframing & Prototypes','Tools','Creating and testing low/high-fidelity prototypes.'),

-- Bob: Frontend Frameworks (4 units)
('4c5d6e7f-8012-4a34-1256-7c8d9e0f1a2b','b49d8d4f-3eab-4efb-80ad-f19d1bca5e1b',1,'React Basics','Components','Building UI with functional components.'),
('5d6e7f80-9123-4b45-2367-8d9e0f1a2b3c','b49d8d4f-3eab-4efb-80ad-f19d1bca5e1b',2,'State & Props','Data Flow','Managing state and component props.'),
('6e7f8091-a234-4c56-3478-9e0f1a2b3c4d','b49d8d4f-3eab-4efb-80ad-f19d1bca5e1b',3,'Routing & SPAs','Navigation','Client-side routing and nested routes.'),
('7f8091a2-b345-4d67-4589-0f1a2b3c4d5e','b49d8d4f-3eab-4efb-80ad-f19d1bca5e1b',4,'Performance','Optimizations','Code-splitting, lazy loading, and best practices.'),

-- Carol: Machine Learning (5 units)
('fb10a111-1a11-4b1a-a111-bb11a1b1a111','c0b2a776-5b19-4b14-8e8e-705708c8e2b9',1,'ML Overview','What is ML?','Intro to machine learning.'),
('fb22a222-2b22-4b2b-b222-bb22b2b2b222','c0b2a776-5b19-4b14-8e8e-705708c8e2b9',2,'Supervised Learning','Labeled data','Regression and classification.'),
('fb33a333-3c33-4b3c-c333-bb33c3c3c333','c0b2a776-5b19-4b14-8e8e-705708c8e2b9',3,'Unsupervised Learning','Clusters','Clustering algorithms.'),
('fb44a444-4d44-4b4d-d444-bb44d4d4d444','c0b2a776-5b19-4b14-8e8e-705708c8e2b9',4,'Reinforcement Learning','Rewards','Agents and environments.'),
('fb55a555-5e55-4b5e-e555-bb55e5e5e555','c0b2a776-5b19-4b14-8e8e-705708c8e2b9',5,'ML Ethics','Bias','Responsible AI.'),

-- David: Cloud Computing (4 units)
('8a9012b3-c456-4e78-569a-1a2b3c4d5e6f','d02c9832-8b3e-4cc7-84a1-5ebeb9c13b12',1,'Cloud Concepts','Basics','IaaS, PaaS, SaaS and core concepts.'),
('9b0123c4-d567-4f89-67ab-2b3c4d5e6f70','d02c9832-8b3e-4cc7-84a1-5ebeb9c13b12',2,'Compute & Storage','Services','Virtual machines, containers, object/block storage.'),
('ab1234d5-e678-40a1-78bc-3c4d5e6f7081','d02c9832-8b3e-4cc7-84a1-5ebeb9c13b12',3,'Networking in Cloud','VPCs',null),
('bc2345e6-f789-41b2-89cd-4d5e6f708192','d02c9832-8b3e-4cc7-84a1-5ebeb9c13b12',4,'Cloud Security','Identity',null),

-- David: Networking Fundamentals (3 units)
('cd3456f7-0891-42c3-9aef-5e6f708192a3','d4acaf91-22df-4206-a373-15de787d48e4',1,'OSI Model','Layers','Understanding OSI and TCP/IP stack.'),
('de456701-1923-43d4-ab01-6f708192a3b4','d4acaf91-22df-4206-a373-15de787d48e4',2,'Routing & Switching','Devices','How routers and switches forward traffic.'),
('ef567812-2a34-44e5-bc12-708192a3b4c5','d4acaf91-22df-4206-a373-15de787d48e4',3,'Subnetting','IP Addressing','CIDR, subnet masks, and calculations.'),

-- David: DevOps Essentials (3 units)
('fa678923-3b45-45f6-cd23-8192a3b4c5d6','d8e1231f-1a56-4739-a9aa-2a68cda3e4b7',1,'CI/CD','Pipelines','Continuous integration and delivery basics.'),
('ab789a34-4c56-46f7-de34-92a3b4c5d6e7','d8e1231f-1a56-4739-a9aa-2a68cda3e4b7',2,'Infrastructure as Code','IaC','Terraform, CloudFormation concepts.'),
('bc89ab45-5d67-47f8-ef45-a3b4c5d6e7f8','d8e1231f-1a56-4739-a9aa-2a68cda3e4b7',3,'Monitoring & Logging','Observability','Prometheus, Grafana and log aggregation.'),

-- Emma: Cybersecurity Basics (3 units)
('fc10a101-1a10-4c1a-a101-bb10a1b1a101','e08da6a4-6bce-4a0f-b0cc-b52b8ab39b89',1,'Cyber Threats','Types','Common threats and attacks.'),
('fc21a202-2b21-4c2b-b202-bb21b2b2b202','e08da6a4-6bce-4a0f-b0cc-b52b8ab39b89',2,'Network Security','Firewalls','Protecting network infrastructure.'),
('fc32a303-3c32-4c3c-c303-bb32c3c3c303','e08da6a4-6bce-4a0f-b0cc-b52b8ab39b89',3,'Best Practices','Prevention','Personal and enterprise security tips.'),

-- Emma: Cloud Infrastructure (4 units)
('cd9ab056-6e78-48a9-8056-b4c5d6e7f8a9','e5e8cc3e-22e3-4b63-802e-92a6ceab5b8b',1,'Virtualization','Hypervisors','VMs, hypervisors, and container differences.'),
('deab0c67-7f89-49ba-9167-c5d6e7f8a9b0','e5e8cc3e-22e3-4b63-802e-92a6ceab5b8b',2,'Storage Solutions','Block/Object','Choosing storage types for workloads.'),
('efbc1d78-809a-4acb-9278-d6e7f8a9b0c1','e5e8cc3e-22e3-4b63-802e-92a6ceab5b8b',3,'Networking','Cloud Networks','Designing networks for cloud deployments.'),
('f0cd2e89-91ab-4bdc-8379-e7f8a9b0c1d2','e5e8cc3e-22e3-4b63-802e-92a6ceab5b8b',4,'High Availability','Resilience','Architectures for uptime and failover.');
