package org.mtcg.application.controller;

import org.mtcg.application.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RestUserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RestUserController restUserController;

}
