package com.example.petree.domain.dog.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

//@ExtendWith(SpringExtension.class)
//public class DogControllerTest {
//
//    private MockMvc mockMvc;
//
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private DogService dogService;
//    @Mock
//    private Response response;
//
//    @InjectMocks
//    private DogController dogController;
//
//    @Test
//    public void testGetAllDogs() throws Exception {
//        objectMapper = new ObjectMapper();
//        mockMvc = MockMvcBuilders.standaloneSetup(dogController).build();
//
//        //given
//        SimpleDogDto dogDto1 = new SimpleDogDto(1L, "쫑이", "토이푸들", Gender.MALE, Status.AVAILABLE, LocalDate.of(2023, 7, 1), "착하고 순해요");
//        SimpleDogDto dogDto2 = new SimpleDogDto(2L, "하늘이", "골든 리트리버", Gender.FEMALE, Status.AVAILABLE, LocalDate.of(2023, 7, 1), "착하고 순하고 커요");
//        List<SimpleDogDto> simpleDogDtoList = Arrays.asList(dogDto1, dogDto2);
//
//        PageImpl<SimpleDogDto> pageableDogs = new PageImpl<>(simpleDogDtoList);
//        Pageable pageable = PageRequest.of(0, 2);
//
//        when(dogService.getDogs(pageable, "", "")).thenReturn(pageableDogs);
//
//        MvcResult result = mockMvc.perform(get("/dogs")
//                        .param("verification", "")
//                        .param("keyword", ""))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        MockHttpServletResponse response = result.getResponse();
//        String jsonStr = response.getContentAsString();
//
//        System.out.println("Response JSON : " + jsonStr);
//
//    }
//}
