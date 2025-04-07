-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `next_services`
-- =============================
INSERT INTO next_services (id, createAt, name) VALUES
(1, strftime('%s', 'now') * 1000, 'A/C'),
(2, strftime('%s', 'now') * 1000, 'A/C Compressor'),
(3, strftime('%s', 'now') * 1000, 'A/C Condenser'),
(4, strftime('%s', 'now') * 1000, 'Air filter'),
(5, strftime('%s', 'now') * 1000, 'Airbag'),
(6, strftime('%s', 'now') * 1000, 'Alignment'),
(7, strftime('%s', 'now') * 1000, 'Axles');

-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `payment_method`
-- =============================
INSERT INTO payment_method (name, createAt) VALUES
('Credit Card', strftime('%s', 'now') * 1000),
('Debit Card', strftime('%s', 'now') * 1000),
('Bank Transfer', strftime('%s', 'now') * 1000),
('Cash', strftime('%s', 'now') * 1000),
('Mobile Payment', strftime('%s', 'now') * 1000),
('E-Wallets', strftime('%s', 'now') * 1000);


-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `fuel_type`
-- =============================
INSERT INTO fuel_type (name, createAt) VALUES
('Petrol', strftime('%s', 'now') * 1000),
('Diesel', strftime('%s', 'now') * 1000),
('Electric', strftime('%s', 'now') * 1000),
('Hybrid', strftime('%s', 'now') * 1000),
('CNG (Compressed Natural Gas)', strftime('%s', 'now') * 1000),
('LPG (Liquefied Petroleum Gas)', strftime('%s', 'now') * 1000),
('Hydrogen', strftime('%s', 'now') * 1000);

-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `expense_type`
-- =============================
INSERT INTO expense_type (name, createAt) VALUES
('Refund/Compensation', strftime('%s', 'now') * 1000),
('Fines/Penalties', strftime('%s', 'now') * 1000),
('Toll Fees', strftime('%s', 'now') * 1000),
('Payments', strftime('%s', 'now') * 1000),
('Taxes', strftime('%s', 'now') * 1000),
('Financing Costs', strftime('%s', 'now') * 1000),
('Inspection Fees', strftime('%s', 'now') * 1000),
('Parking Fees', strftime('%s', 'now') * 1000),
('Insurance Costs', strftime('%s', 'now') * 1000);

-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `service_type`
-- =============================
INSERT INTO service_type (name, createAt) VALUES
('Engine Oil Replacement', strftime('%s', 'now') * 1000),
('Transmission Fluid Check and Replacement', strftime('%s', 'now') * 1000),
('Brake Pad Replacement', strftime('%s', 'now') * 1000),
('Brake Fluid Check and Replacement', strftime('%s', 'now') * 1000),
('Coolant Flush and Replacement', strftime('%s', 'now') * 1000),
('Spark Plug Replacement', strftime('%s', 'now') * 1000),
('Fuel Filter Replacement', strftime('%s', 'now') * 1000),
('Tire Rotation and Balancing', strftime('%s', 'now') * 1000),
('Wheel Alignment', strftime('%s', 'now') * 1000);

-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `income_type`
-- =============================
INSERT INTO income_type (name, createAt) VALUES
('Freight Transport', strftime('%s', 'now') * 1000),
('Driving Services', strftime('%s', 'now') * 1000),
('Car Rental', strftime('%s', 'now') * 1000),
('Passenger Transport', strftime('%s', 'now') * 1000),
('Food Delivery', strftime('%s', 'now') * 1000),
('Passenger Transportation', strftime('%s', 'now') * 1000),
('Wedding Car Services', strftime('%s', 'now') * 1000),
('Tourist Transportation', strftime('%s', 'now') * 1000),
('Event Transportation Services', strftime('%s', 'now') * 1000);

-- =============================
-- VEHICLE
-- =============================
-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `vehicle_type`
-- =============================
INSERT INTO vehicle_type (name, resourceName, createAt) VALUES
('Car', 'v_sedan', strftime('%s', 'now') * 1000),
--('Motorcycle', 'v_motorbike', strftime('%s', 'now') * 1000),
('Truck', 'v_lorry', strftime('%s', 'now') * 1000),
('Bus', 'v_school_bus', strftime('%s', 'now') * 1000);


-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `manufacturer`
-- =============================
INSERT INTO manufacturer (name, resourceName, createAt) VALUES
('9Ff', 'brand_9ff', strftime('%s', 'now') * 1000),
('Abadal', 'brand_abadal', strftime('%s', 'now') * 1000),
('Abarth', 'brand_abarth', strftime('%s', 'now') * 1000),
('Abt', 'brand_abt', strftime('%s', 'now') * 1000),
('Ac', 'brand_ac', strftime('%s', 'now') * 1000),
('Acura', 'brand_acura', strftime('%s', 'now') * 1000),
('Aixam', 'brand_aixam', strftime('%s', 'now') * 1000),
('Alfa Romeo', 'brand_alfa_romeo', strftime('%s', 'now') * 1000),
('Alpina', 'brand_alpina', strftime('%s', 'now') * 1000),
('Alpine', 'brand_alpine', strftime('%s', 'now') * 1000),
('Alvis', 'brand_alvis', strftime('%s', 'now') * 1000),
('American Motors', 'brand_american_motors', strftime('%s', 'now') * 1000),
('Amg', 'brand_amg', strftime('%s', 'now') * 1000),
('Apollo', 'brand_apollo', strftime('%s', 'now') * 1000),
('Arash', 'brand_arash', strftime('%s', 'now') * 1000),
('Arcfox', 'brand_arcfox', strftime('%s', 'now') * 1000),
('Ariel', 'brand_ariel', strftime('%s', 'now') * 1000),
('Aro', 'brand_aro', strftime('%s', 'now') * 1000),
('Arrinera', 'brand_arrinera', strftime('%s', 'now') * 1000),
('Arrival', 'brand_arrival', strftime('%s', 'now') * 1000),
('Artega', 'brand_artega', strftime('%s', 'now') * 1000),
('Ascari', 'brand_ascari', strftime('%s', 'now') * 1000),
('Askam', 'brand_askam', strftime('%s', 'now') * 1000),
('Aspark', 'brand_aspark', strftime('%s', 'now') * 1000),
('Aston Martin', 'brand_aston_martin', strftime('%s', 'now') * 1000),
('Atalanta Motors', 'brand_atalanta_motors', strftime('%s', 'now') * 1000),
('Audi', 'brand_audi', strftime('%s', 'now') * 1000),
('Austin', 'brand_austin', strftime('%s', 'now') * 1000),
('Autobacs', 'brand_autobacs', strftime('%s', 'now') * 1000),
('Autobianchi', 'brand_autobianchi', strftime('%s', 'now') * 1000),
('Axon', 'brand_axon', strftime('%s', 'now') * 1000),
('Bac', 'brand_bac', strftime('%s', 'now') * 1000),
('Baic Motor', 'brand_baic_motor', strftime('%s', 'now') * 1000),
('Baojun', 'brand_baojun', strftime('%s', 'now') * 1000),
('Beiben', 'brand_beiben', strftime('%s', 'now') * 1000),
('Bentley', 'brand_bentley', strftime('%s', 'now') * 1000),
('Berliet', 'brand_berliet', strftime('%s', 'now') * 1000),
('Bertone', 'brand_bertone', strftime('%s', 'now') * 1000),
('Bestune', 'brand_bestune', strftime('%s', 'now') * 1000),
('Bharatbenz', 'brand_bharatbenz', strftime('%s', 'now') * 1000),
('Bitter', 'brand_bitter', strftime('%s', 'now') * 1000),
('Bmw', 'brand_bmw', strftime('%s', 'now') * 1000),
('Bmw M', 'brand_bmw_m', strftime('%s', 'now') * 1000),
('Borgward', 'brand_borgward', strftime('%s', 'now') * 1000),
('Bowler', 'brand_bowler', strftime('%s', 'now') * 1000),
('Brabus', 'brand_brabus', strftime('%s', 'now') * 1000),
('Brammo', 'brand_brammo', strftime('%s', 'now') * 1000),
('Brilliance', 'brand_brilliance', strftime('%s', 'now') * 1000),
('Brooke', 'brand_brooke', strftime('%s', 'now') * 1000),
('Bufori', 'brand_bufori', strftime('%s', 'now') * 1000),
('Bugatti', 'brand_bugatti', strftime('%s', 'now') * 1000),
('Buick', 'brand_buick', strftime('%s', 'now') * 1000),
('Byd', 'brand_byd', strftime('%s', 'now') * 1000),
('Byton', 'brand_byton', strftime('%s', 'now') * 1000),
('Cadillac', 'brand_cadillac', strftime('%s', 'now') * 1000),
('Camc', 'brand_camc', strftime('%s', 'now') * 1000),
('Canoo', 'brand_canoo', strftime('%s', 'now') * 1000),
('Caparo', 'brand_caparo', strftime('%s', 'now') * 1000),
('Carlsson', 'brand_carlsson', strftime('%s', 'now') * 1000),
('Caterham', 'brand_caterham', strftime('%s', 'now') * 1000),
('Changan', 'brand_changan', strftime('%s', 'now') * 1000),
('Changfeng', 'brand_changfeng', strftime('%s', 'now') * 1000),
('Chery', 'brand_chery', strftime('%s', 'now') * 1000),
('Chevrolet', 'brand_chevrolet', strftime('%s', 'now') * 1000),
('Chrysler', 'brand_chrysler', strftime('%s', 'now') * 1000),
('Citroen', 'brand_citroen', strftime('%s', 'now') * 1000),
('Cizeta', 'brand_cizeta', strftime('%s', 'now') * 1000),
('Corvette', 'brand_corvette', strftime('%s', 'now') * 1000),
('Dacia', 'brand_dacia', strftime('%s', 'now') * 1000),
('Daewoo', 'brand_daewoo', strftime('%s', 'now') * 1000),
('Daf', 'brand_daf', strftime('%s', 'now') * 1000),
('Daihatsu', 'brand_daihatsu', strftime('%s', 'now') * 1000),
('Daimler', 'brand_daimler', strftime('%s', 'now') * 1000),
('Datsun', 'brand_datsun', strftime('%s', 'now') * 1000),
('David Brown', 'brand_david_brown', strftime('%s', 'now') * 1000),
('Dayun', 'brand_dayun', strftime('%s', 'now') * 1000),
('Delage', 'brand_delage', strftime('%s', 'now') * 1000),
('Desoto', 'brand_desoto', strftime('%s', 'now') * 1000),
('Detroit Electric', 'brand_detroit_electric', strftime('%s', 'now') * 1000),
('Devel Sixteen', 'brand_devel_sixteen', strftime('%s', 'now') * 1000),
('Dina', 'brand_dina', strftime('%s', 'now') * 1000),
('Dodge', 'brand_dodge', strftime('%s', 'now') * 1000),
('Dongfeng', 'brand_dongfeng', strftime('%s', 'now') * 1000),
('Donkervoort', 'brand_donkervoort', strftime('%s', 'now') * 1000),
('Drako', 'brand_drako', strftime('%s', 'now') * 1000),
('Ds', 'brand_ds', strftime('%s', 'now') * 1000),
('Duesenberg', 'brand_duesenberg', strftime('%s', 'now') * 1000),
('Eagle', 'brand_eagle', strftime('%s', 'now') * 1000),
('Edag', 'brand_edag', strftime('%s', 'now') * 1000),
('Edsel', 'brand_edsel', strftime('%s', 'now') * 1000),
('Eicher', 'brand_eicher', strftime('%s', 'now') * 1000),
('Elemental', 'brand_elemental', strftime('%s', 'now') * 1000),
('Englon', 'brand_englon', strftime('%s', 'now') * 1000),
('Erf', 'brand_erf', strftime('%s', 'now') * 1000),
('Exeed', 'brand_exeed', strftime('%s', 'now') * 1000),
('Faraday Future', 'brand_faraday_future', strftime('%s', 'now') * 1000),
('Faw', 'brand_faw', strftime('%s', 'now') * 1000),
('Ferrari', 'brand_ferrari', strftime('%s', 'now') * 1000),
('Fiat', 'brand_fiat', strftime('%s', 'now') * 1000),
('Fisker', 'brand_fisker', strftime('%s', 'now') * 1000),
('Foden', 'brand_foden', strftime('%s', 'now') * 1000),
('Force', 'brand_force', strftime('%s', 'now') * 1000),
('Ford', 'brand_ford', strftime('%s', 'now') * 1000),
('Foton', 'brand_foton', strftime('%s', 'now') * 1000),
('Freightliner', 'brand_freightliner', strftime('%s', 'now') * 1000),
('Fso', 'brand_fso', strftime('%s', 'now') * 1000),
('Gac', 'brand_gac', strftime('%s', 'now') * 1000),
('Gardner Douglas', 'brand_gardner_douglas', strftime('%s', 'now') * 1000),
('Gaz', 'brand_gaz', strftime('%s', 'now') * 1000),
('Geely', 'brand_geely', strftime('%s', 'now') * 1000),
('General Motors', 'brand_general_motors', strftime('%s', 'now') * 1000),
('Genesis', 'brand_genesis', strftime('%s', 'now') * 1000),
('Geo', 'brand_geo', strftime('%s', 'now') * 1000),
('Geometry', 'brand_geometry', strftime('%s', 'now') * 1000),
('Ginetta', 'brand_ginetta', strftime('%s', 'now') * 1000),
('Gmc', 'brand_gmc', strftime('%s', 'now') * 1000),
('Golden', 'brand_golden', strftime('%s', 'now') * 1000),
('Gonow', 'brand_gonow', strftime('%s', 'now') * 1000),
('Great Wall', 'brand_great_wall', strftime('%s', 'now') * 1000),
('Grinnall', 'brand_grinnall', strftime('%s', 'now') * 1000),
('Gt R', 'brand_gt_r', strftime('%s', 'now') * 1000),
('Gta Motor', 'brand_gta_motor', strftime('%s', 'now') * 1000),
('Gumpert', 'brand_gumpert', strftime('%s', 'now') * 1000),
('Hafei', 'brand_hafei', strftime('%s', 'now') * 1000),
('Haima', 'brand_haima', strftime('%s', 'now') * 1000),
('Haval', 'brand_haval', strftime('%s', 'now') * 1000),
('Hawtai', 'brand_hawtai', strftime('%s', 'now') * 1000),
('Hennessey', 'brand_hennessey', strftime('%s', 'now') * 1000),
('Higer', 'brand_higer', strftime('%s', 'now') * 1000),
('Hino', 'brand_hino', strftime('%s', 'now') * 1000),
('Holden', 'brand_holden', strftime('%s', 'now') * 1000),
('Hommell', 'brand_hommell', strftime('%s', 'now') * 1000),
('Honda', 'brand_honda', strftime('%s', 'now') * 1000),
('Hongqi', 'brand_hongqi', strftime('%s', 'now') * 1000),
('Hongyan', 'brand_hongyan', strftime('%s', 'now') * 1000),
('Horch', 'brand_horch', strftime('%s', 'now') * 1000),
('Hsv', 'brand_hsv', strftime('%s', 'now') * 1000),
('Hudson', 'brand_hudson', strftime('%s', 'now') * 1000),
('Hummer', 'brand_hummer', strftime('%s', 'now') * 1000),
('Hyundai', 'brand_hyundai', strftime('%s', 'now') * 1000),
('Ic Bus', 'brand_ic_bus', strftime('%s', 'now') * 1000),
('Infiniti', 'brand_infiniti', strftime('%s', 'now') * 1000),
('International Harvester', 'brand_international_harvester', strftime('%s', 'now') * 1000),
('International Trucks', 'brand_international_trucks', strftime('%s', 'now') * 1000),
('Iran Khodro', 'brand_iran_khodro', strftime('%s', 'now') * 1000),
('Irizar', 'brand_irizar', strftime('%s', 'now') * 1000),
('Iso', 'brand_iso', strftime('%s', 'now') * 1000),
('Isuzu', 'brand_isuzu', strftime('%s', 'now') * 1000),
('Iveco', 'brand_iveco', strftime('%s', 'now') * 1000),
('Jac Motors', 'brand_jac_motors', strftime('%s', 'now') * 1000),
('Jaguar', 'brand_jaguar', strftime('%s', 'now') * 1000),
('Jba Motors', 'brand_jba_motors', strftime('%s', 'now') * 1000),
('Jeep', 'brand_jeep', strftime('%s', 'now') * 1000),
('Jetta', 'brand_jetta', strftime('%s', 'now') * 1000),
('Jiangling', 'brand_jiangling', strftime('%s', 'now') * 1000),
('Kamaz', 'brand_kamaz', strftime('%s', 'now') * 1000),
('Karlmann King', 'brand_karlmann_king', strftime('%s', 'now') * 1000),
('Karma', 'brand_karma', strftime('%s', 'now') * 1000),
('Keating Supercars', 'brand_keating_supercars', strftime('%s', 'now') * 1000),
('Kenworth', 'brand_kenworth', strftime('%s', 'now') * 1000),
('Kia', 'brand_kia', strftime('%s', 'now') * 1000),
('King Long', 'brand_king_long', strftime('%s', 'now') * 1000),
('Koenigsegg', 'brand_koenigsegg', strftime('%s', 'now') * 1000),
('Ktm', 'brand_ktm', strftime('%s', 'now') * 1000),
('Lada', 'brand_lada', strftime('%s', 'now') * 1000),
('Lagonda', 'brand_lagonda', strftime('%s', 'now') * 1000),
('Lamborghini', 'brand_lamborghini', strftime('%s', 'now') * 1000),
('Lancia', 'brand_lancia', strftime('%s', 'now') * 1000),
('Land Rover', 'brand_land_rover', strftime('%s', 'now') * 1000),
('Landwind', 'brand_landwind', strftime('%s', 'now') * 1000),
('Laraki', 'brand_laraki', strftime('%s', 'now') * 1000),
('Lexus', 'brand_lexus', strftime('%s', 'now') * 1000),
('Leyland', 'brand_leyland', strftime('%s', 'now') * 1000),
('Lifan', 'brand_lifan', strftime('%s', 'now') * 1000),
('Ligier', 'brand_ligier', strftime('%s', 'now') * 1000),
('Lincoln', 'brand_lincoln', strftime('%s', 'now') * 1000),
('Lister', 'brand_lister', strftime('%s', 'now') * 1000),
('Lixiang', 'brand_lixiang', strftime('%s', 'now') * 1000),
('Lloyd', 'brand_lloyd', strftime('%s', 'now') * 1000),
('Lobini', 'brand_lobini', strftime('%s', 'now') * 1000),
('London Ev', 'brand_london_ev', strftime('%s', 'now') * 1000),
('Lordstown', 'brand_lordstown', strftime('%s', 'now') * 1000),
('Lordstown 1', 'brand_lordstown_1', strftime('%s', 'now') * 1000),
('Lotus', 'brand_lotus', strftime('%s', 'now') * 1000),
('Lucid', 'brand_lucid', strftime('%s', 'now') * 1000),
('Luxgen', 'brand_luxgen', strftime('%s', 'now') * 1000),
('Lynkco', 'brand_lynkco', strftime('%s', 'now') * 1000),
('Mack', 'brand_mack', strftime('%s', 'now') * 1000),
('Mahindra', 'brand_mahindra', strftime('%s', 'now') * 1000),
('Man', 'brand_man', strftime('%s', 'now') * 1000),
('Mansory', 'brand_mansory', strftime('%s', 'now') * 1000),
('Marlin', 'brand_marlin', strftime('%s', 'now') * 1000),
('Maserati', 'brand_maserati', strftime('%s', 'now') * 1000),
('Mastretta', 'brand_mastretta', strftime('%s', 'now') * 1000),
('Maxus', 'brand_maxus', strftime('%s', 'now') * 1000),
('Maybach', 'brand_maybach', strftime('%s', 'now') * 1000),
('Maz', 'brand_maz', strftime('%s', 'now') * 1000),
('Mazda', 'brand_mazda', strftime('%s', 'now') * 1000),
('Mazzanti Automobili', 'brand_mazzanti_automobili', strftime('%s', 'now') * 1000),
('Mclaren', 'brand_mclaren', strftime('%s', 'now') * 1000),
('Melkus', 'brand_melkus', strftime('%s', 'now') * 1000),
('Mercedes Benz', 'brand_mercedes_benz', strftime('%s', 'now') * 1000),
('Mercury', 'brand_mercury', strftime('%s', 'now') * 1000),
('Merkur', 'brand_merkur', strftime('%s', 'now') * 1000),
('Mg', 'brand_mg', strftime('%s', 'now') * 1000),
('Microcar', 'brand_microcar', strftime('%s', 'now') * 1000),
('Mills Extreme Vehicles', 'brand_mills_extreme_vehicles', strftime('%s', 'now') * 1000),
('Mini', 'brand_mini', strftime('%s', 'now') * 1000),
('Mitsubishi', 'brand_mitsubishi', strftime('%s', 'now') * 1000),
('Mitsuoka', 'brand_mitsuoka', strftime('%s', 'now') * 1000),
('Mk Sportscars', 'brand_mk_sportscars', strftime('%s', 'now') * 1000),
('Morgan', 'brand_morgan', strftime('%s', 'now') * 1000),
('Navistar', 'brand_navistar', strftime('%s', 'now') * 1000),
('Nevs', 'brand_nevs', strftime('%s', 'now') * 1000),
('Nikola', 'brand_nikola', strftime('%s', 'now') * 1000),
('Nio', 'brand_nio', strftime('%s', 'now') * 1000),
('Nismo', 'brand_nismo', strftime('%s', 'now') * 1000),
('Nissan', 'brand_nissan', strftime('%s', 'now') * 1000),
('Noble', 'brand_noble', strftime('%s', 'now') * 1000),
('Oldsmobile', 'brand_oldsmobile', strftime('%s', 'now') * 1000),
('Opel', 'brand_opel', strftime('%s', 'now') * 1000),
('Paccar', 'brand_paccar', strftime('%s', 'now') * 1000),
('Packard', 'brand_packard', strftime('%s', 'now') * 1000),
('Pagani', 'brand_pagani', strftime('%s', 'now') * 1000),
('Panoz', 'brand_panoz', strftime('%s', 'now') * 1000),
('Pegaso', 'brand_pegaso', strftime('%s', 'now') * 1000),
('Perodua', 'brand_perodua', strftime('%s', 'now') * 1000),
('Peterbilt', 'brand_peterbilt', strftime('%s', 'now') * 1000),
('Peugeot', 'brand_peugeot', strftime('%s', 'now') * 1000),
('Pininfarina', 'brand_pininfarina', strftime('%s', 'now') * 1000),
('Plymouth', 'brand_plymouth', strftime('%s', 'now') * 1000),
('Polestar', 'brand_polestar', strftime('%s', 'now') * 1000),
('Pontiac', 'brand_pontiac', strftime('%s', 'now') * 1000),
('Porsche', 'brand_porsche', strftime('%s', 'now') * 1000),
('Praga', 'brand_praga', strftime('%s', 'now') * 1000),
('Premier', 'brand_premier', strftime('%s', 'now') * 1000),
('Prodrive', 'brand_prodrive', strftime('%s', 'now') * 1000),
('Proton', 'brand_proton', strftime('%s', 'now') * 1000),
('Qoros', 'brand_qoros', strftime('%s', 'now') * 1000),
('Radical', 'brand_radical', strftime('%s', 'now') * 1000),
('Ram', 'brand_ram', strftime('%s', 'now') * 1000),
('Rambler', 'brand_rambler', strftime('%s', 'now') * 1000),
('Ranz', 'brand_ranz', strftime('%s', 'now') * 1000),
('Renault', 'brand_renault', strftime('%s', 'now') * 1000),
('Renault Samsung Motors', 'brand_renault_samsung_motors', strftime('%s', 'now') * 1000),
('Rezvani', 'brand_rezvani', strftime('%s', 'now') * 1000),
('Rimac', 'brand_rimac', strftime('%s', 'now') * 1000),
('Rinspeed', 'brand_rinspeed', strftime('%s', 'now') * 1000),
('Rivian', 'brand_rivian', strftime('%s', 'now') * 1000),
('Roewe', 'brand_roewe', strftime('%s', 'now') * 1000),
('Rolls Royce', 'brand_rolls_royce', strftime('%s', 'now') * 1000),
('Ronart', 'brand_ronart', strftime('%s', 'now') * 1000),
('Rossion', 'brand_rossion', strftime('%s', 'now') * 1000),
('Rover', 'brand_rover', strftime('%s', 'now') * 1000),
('Ruf', 'brand_ruf', strftime('%s', 'now') * 1000),
('Saab', 'brand_saab', strftime('%s', 'now') * 1000),
('Saic Motor', 'brand_saic_motor', strftime('%s', 'now') * 1000),
('Saipa', 'brand_saipa', strftime('%s', 'now') * 1000),
('Saturn', 'brand_saturn', strftime('%s', 'now') * 1000),
('Scania', 'brand_scania', strftime('%s', 'now') * 1000),
('Scion', 'brand_scion', strftime('%s', 'now') * 1000),
('Seat', 'brand_seat', strftime('%s', 'now') * 1000),
('Setra', 'brand_setra', strftime('%s', 'now') * 1000),
('Shacman', 'brand_shacman', strftime('%s', 'now') * 1000),
('Simca', 'brand_simca', strftime('%s', 'now') * 1000),
('Singulato', 'brand_singulato', strftime('%s', 'now') * 1000),
('Sinotruk', 'brand_sinotruk', strftime('%s', 'now') * 1000),
('Sisu', 'brand_sisu', strftime('%s', 'now') * 1000),
('Skoda', 'brand_skoda', strftime('%s', 'now') * 1000),
('Smart', 'brand_smart', strftime('%s', 'now') * 1000),
('Soueast', 'brand_soueast', strftime('%s', 'now') * 1000),
('Spirra', 'brand_spirra', strftime('%s', 'now') * 1000),
('Spyker', 'brand_spyker', strftime('%s', 'now') * 1000),
('Ssangyong', 'brand_ssangyong', strftime('%s', 'now') * 1000),
('Ssc', 'brand_ssc', strftime('%s', 'now') * 1000),
('Sterling', 'brand_sterling', strftime('%s', 'now') * 1000),
('Studebaker', 'brand_studebaker', strftime('%s', 'now') * 1000),
('Stutz', 'brand_stutz', strftime('%s', 'now') * 1000),
('Subaru', 'brand_subaru', strftime('%s', 'now') * 1000),
('Suffolk Sportscars', 'brand_suffolk_sportscars', strftime('%s', 'now') * 1000),
('Suzuki', 'brand_suzuki', strftime('%s', 'now') * 1000),
('Talbot', 'brand_talbot', strftime('%s', 'now') * 1000),
('Tata', 'brand_tata', strftime('%s', 'now') * 1000),
('Tatra', 'brand_tatra', strftime('%s', 'now') * 1000),
('Techart', 'brand_techart', strftime('%s', 'now') * 1000),
('Tesla', 'brand_tesla', strftime('%s', 'now') * 1000),
('Toyota', 'brand_toyota', strftime('%s', 'now') * 1000),
('Toyota Alphard', 'brand_toyota_alphard', strftime('%s', 'now') * 1000),
('Toyota Century', 'brand_toyota_century', strftime('%s', 'now') * 1000),
('Toyota Crown', 'brand_toyota_crown', strftime('%s', 'now') * 1000),
('Tramontana', 'brand_tramontana', strftime('%s', 'now') * 1000),
('Trion', 'brand_trion', strftime('%s', 'now') * 1000),
('Triumph', 'brand_triumph', strftime('%s', 'now') * 1000),
('Troller', 'brand_troller', strftime('%s', 'now') * 1000),
('Tucker', 'brand_tucker', strftime('%s', 'now') * 1000),
('Tvr', 'brand_tvr', strftime('%s', 'now') * 1000),
('Uaz', 'brand_uaz', strftime('%s', 'now') * 1000),
('Ud Trucks', 'brand_ud_trucks', strftime('%s', 'now') * 1000),
('Ultima', 'brand_ultima', strftime('%s', 'now') * 1000),
('Vauxhall', 'brand_vauxhall', strftime('%s', 'now') * 1000),
('Vector Motors', 'brand_vector_motors', strftime('%s', 'now') * 1000),
('Vencer', 'brand_vencer', strftime('%s', 'now') * 1000),
('Venucia', 'brand_venucia', strftime('%s', 'now') * 1000),
('Vinfast', 'brand_vinfast', strftime('%s', 'now') * 1000),
('Viper', 'brand_viper', strftime('%s', 'now') * 1000),
('Volkswagen', 'brand_volkswagen', strftime('%s', 'now') * 1000),
('Volvo', 'brand_volvo', strftime('%s', 'now') * 1000),
('W Motors', 'brand_w_motors', strftime('%s', 'now') * 1000),
('Wanderer', 'brand_wanderer', strftime('%s', 'now') * 1000),
('Wartburg', 'brand_wartburg', strftime('%s', 'now') * 1000),
('Weltmeister', 'brand_weltmeister', strftime('%s', 'now') * 1000),
('Western Star', 'brand_western_star', strftime('%s', 'now') * 1000),
('Westfield', 'brand_westfield', strftime('%s', 'now') * 1000),
('Wey', 'brand_wey', strftime('%s', 'now') * 1000),
('Wiesmann', 'brand_wiesmann', strftime('%s', 'now') * 1000),
('Willys Overland', 'brand_willys_overland', strftime('%s', 'now') * 1000),
('Workhorse', 'brand_workhorse', strftime('%s', 'now') * 1000),
('Wuling', 'brand_wuling', strftime('%s', 'now') * 1000),
('Xpeng', 'brand_xpeng', strftime('%s', 'now') * 1000),
('Yulon', 'brand_yulon', strftime('%s', 'now') * 1000),
('Yutong', 'brand_yutong', strftime('%s', 'now') * 1000),
('Zaz', 'brand_zaz', strftime('%s', 'now') * 1000),
('Zenos Cars', 'brand_zenos_cars', strftime('%s', 'now') * 1000),
('Zenvo', 'brand_zenvo', strftime('%s', 'now') * 1000),
('Zhinuo', 'brand_zhinuo', strftime('%s', 'now') * 1000),
('Zhongtong', 'brand_zhongtong', strftime('%s', 'now') * 1000),
('Zotye', 'brand_zotye', strftime('%s', 'now') * 1000);

-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `type_vehicle_style`
-- =============================
INSERT INTO type_vehicle_style (name, createAt) VALUES
('SUV', strftime('%s', 'now') * 1000),
('Sedan', strftime('%s', 'now') * 1000),
('Coupe', strftime('%s', 'now') * 1000),
('Hatchback', strftime('%s', 'now') * 1000),
('Pickup', strftime('%s', 'now') * 1000);


-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `color_style`
-- =============================
INSERT INTO color_style (name, code, createAt) VALUES
('Black', "#000000", strftime('%s', 'now') * 1000),
('White', "#FFFFFF", strftime('%s', 'now') * 1000),
('Blue', "#152190", strftime('%s', 'now') * 1000),
('Red', "#871010", strftime('%s', 'now') * 1000),
('Green', "#4F5838", strftime('%s', 'now') * 1000),
('Brown', "#574500", strftime('%s', 'now') * 1000);

-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `type_vehicle_style_color_style_cross_ref`
-- =============================
-- =============================
-- CHÈN DỮ LIỆU VÀO BẢNG `type_vehicle_style_color_style_cross_ref`
-- =============================

INSERT INTO type_vehicle_style_color_style_cross_ref (typeVehicleStyleId, colorStyleId, resourceName) VALUES
((SELECT id FROM type_vehicle_style WHERE name = 'SUV'), (SELECT id FROM color_style WHERE name = 'Black'), 'suv_black'),
((SELECT id FROM type_vehicle_style WHERE name = 'SUV'), (SELECT id FROM color_style WHERE name = 'White'), 'suv_white'),
((SELECT id FROM type_vehicle_style WHERE name = 'SUV'), (SELECT id FROM color_style WHERE name = 'Blue'), 'suv_blue'),
((SELECT id FROM type_vehicle_style WHERE name = 'SUV'), (SELECT id FROM color_style WHERE name = 'Red'), 'suv_red'),
((SELECT id FROM type_vehicle_style WHERE name = 'SUV'), (SELECT id FROM color_style WHERE name = 'Green'), 'suv_green'),
((SELECT id FROM type_vehicle_style WHERE name = 'SUV'), (SELECT id FROM color_style WHERE name = 'Brown'), 'suv_brown'),

((SELECT id FROM type_vehicle_style WHERE name = 'Sedan'), (SELECT id FROM color_style WHERE name = 'Black'), 'sedan_black'),
((SELECT id FROM type_vehicle_style WHERE name = 'Sedan'), (SELECT id FROM color_style WHERE name = 'White'), 'sedan_white'),
((SELECT id FROM type_vehicle_style WHERE name = 'Sedan'), (SELECT id FROM color_style WHERE name = 'Blue'), 'sedan_blue'),
((SELECT id FROM type_vehicle_style WHERE name = 'Sedan'), (SELECT id FROM color_style WHERE name = 'Red'), 'sedan_red'),
((SELECT id FROM type_vehicle_style WHERE name = 'Sedan'), (SELECT id FROM color_style WHERE name = 'Green'), 'sedan_green'),
((SELECT id FROM type_vehicle_style WHERE name = 'Sedan'), (SELECT id FROM color_style WHERE name = 'Brown'), 'sedan_brown'),

((SELECT id FROM type_vehicle_style WHERE name = 'Coupe'), (SELECT id FROM color_style WHERE name = 'Black'), 'coupe_black'),
((SELECT id FROM type_vehicle_style WHERE name = 'Coupe'), (SELECT id FROM color_style WHERE name = 'White'), 'coupe_white'),
((SELECT id FROM type_vehicle_style WHERE name = 'Coupe'), (SELECT id FROM color_style WHERE name = 'Blue'), 'coupe_blue'),
((SELECT id FROM type_vehicle_style WHERE name = 'Coupe'), (SELECT id FROM color_style WHERE name = 'Red'), 'coupe_red'),
((SELECT id FROM type_vehicle_style WHERE name = 'Coupe'), (SELECT id FROM color_style WHERE name = 'Green'), 'coupe_green'),
((SELECT id FROM type_vehicle_style WHERE name = 'Coupe'), (SELECT id FROM color_style WHERE name = 'Brown'), 'coupe_brown'),

((SELECT id FROM type_vehicle_style WHERE name = 'Hatchback'), (SELECT id FROM color_style WHERE name = 'Black'), 'hatchback_black'),
((SELECT id FROM type_vehicle_style WHERE name = 'Hatchback'), (SELECT id FROM color_style WHERE name = 'White'), 'hatchback_white'),
((SELECT id FROM type_vehicle_style WHERE name = 'Hatchback'), (SELECT id FROM color_style WHERE name = 'Blue'), 'hatchback_blue'),
((SELECT id FROM type_vehicle_style WHERE name = 'Hatchback'), (SELECT id FROM color_style WHERE name = 'Red'), 'hatchback_red'),
((SELECT id FROM type_vehicle_style WHERE name = 'Hatchback'), (SELECT id FROM color_style WHERE name = 'Green'), 'hatchback_green'),
((SELECT id FROM type_vehicle_style WHERE name = 'Hatchback'), (SELECT id FROM color_style WHERE name = 'Brown'), 'hatchback_brown'),

((SELECT id FROM type_vehicle_style WHERE name = 'Pickup'), (SELECT id FROM color_style WHERE name = 'Black'), 'pickup_black'),
((SELECT id FROM type_vehicle_style WHERE name = 'Pickup'), (SELECT id FROM color_style WHERE name = 'White'), 'pickup_white'),
((SELECT id FROM type_vehicle_style WHERE name = 'Pickup'), (SELECT id FROM color_style WHERE name = 'Blue'), 'pickup_blue'),
((SELECT id FROM type_vehicle_style WHERE name = 'Pickup'), (SELECT id FROM color_style WHERE name = 'Red'), 'pickup_red'),
((SELECT id FROM type_vehicle_style WHERE name = 'Pickup'), (SELECT id FROM color_style WHERE name = 'Green'), 'pickup_green'),
((SELECT id FROM type_vehicle_style WHERE name = 'Pickup'), (SELECT id FROM color_style WHERE name = 'Brown'), 'pickup_brown');


-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `location_type`
-- =============================
INSERT INTO location_type (name, resourceName, createAt) VALUES
('Company', 'ic_location_type_company', strftime('%s', 'now') * 1000),
('Home', 'ic_location_type_home', strftime('%s', 'now') * 1000),
('Supermarket', 'ic_location_type_company', strftime('%s', 'now') * 1000),
('Repair Shop', 'ic_location_type_repair_shop', strftime('%s', 'now') * 1000),
('Gas Station', 'ic_location_type_gas_station', strftime('%s', 'now') * 1000),
('Maintenance Center', 'ic_location_type_maintenance_center', strftime('%s', 'now') * 1000),
('Vehicle Inspection Center', 'ic_location_type_vehicle_inspection_center', strftime('%s', 'now') * 1000),
('Insurance Office', 'ic_location_type_insurance_office', strftime('%s', 'now') * 1000),
('Tax Office', 'ic_location_type_tax_office', strftime('%s', 'now') * 1000),
('Parking Facility', 'ic_location_type_parking_facility', strftime('%s', 'now') * 1000),
('Favorite Route', 'ic_location_type_normal', strftime('%s', 'now') * 1000);


-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `default_currency`
-- =============================
INSERT INTO default_currency (name, code, createAt) VALUES
('United States Dollar', 'USD', strftime('%s', 'now') * 1000),
('Euro', 'EUR', strftime('%s', 'now') * 1000),
('Japanese Yen', 'JPY', strftime('%s', 'now') * 1000),
('Chinese Yuan', 'CNY', strftime('%s', 'now') * 1000),
('Australian Dollar', 'AUD', strftime('%s', 'now') * 1000),
('Vietnamese Dong', 'VND', strftime('%s', 'now') * 1000);

-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `preferred_distance_unit`
-- =============================
INSERT INTO preferred_distance_unit (name, unit, createAt) VALUES
('Kilometers', 'km', strftime('%s', 'now') * 1000),
('Miles', 'm', strftime('%s', 'now') * 1000);

-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `fuel_volume_unit`
-- =============================
INSERT INTO fuel_volume_unit (name, code, createAt) VALUES
('Litres', 'L', strftime('%s', 'now') * 1000),
('Gallons', 'US/UK', strftime('%s', 'now') * 1000),
('kWh', 'kWh', strftime('%s', 'now') * 1000);


-- =============================
-- MẪU DỮ LIỆU CHO BẢNG `tracking_setting`
-- =============================
INSERT INTO tracking_setting (idPreferredDistanceUnit, idFuelVolumeUnit, idDefaultCurrency, createAt) VALUES
(1 ,1 ,1 , strftime('%s', 'now') * 1000);


