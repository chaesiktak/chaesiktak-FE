package com.example.chaesiktak

object SampleRecipes {
    val recipes = listOf(
        RecommendRecipe(
            image = R.drawable.sample_image1,
            title = "비건 김치찌개",
            subtext = "1인분, 20분",
            kcal = "250kcal",
            tag = "비건",
            prevtext = "비건 김치찌개 레시피",
            isFavorite = false,
            ingredients = listOf(
                Ingredient("배추김치", "200g"),
                Ingredient("두부", "1/2모"),
                Ingredient("양파", "1개"),
                Ingredient("다진 마늘", "1큰술"),
                Ingredient("고춧가루", "1큰술"),
                Ingredient("소금", "약간")
            ),
            contents = listOf(
                RecipeStep(1, "냄비에 물을 넣고 끓인 후, 김치와 양파를 넣어 5분간 끓입니다."),
                RecipeStep(2, "다진 마늘과 고춧가루를 넣고 잘 저어줍니다."),
                RecipeStep(3, "두부를 먹기 좋은 크기로 잘라 넣고 3분간 더 끓입니다."),
                RecipeStep(4, "소금으로 간을 맞춘 후 완성된 김치찌개를 그릇에 담아 즐기세요!"),
            )
        ),
        RecommendRecipe(
            image = R.drawable.sample_image2,
            title = "채소 카레",
            subtext = "4인분, 50분",
            kcal = "380kcal",
            tag = "락토",
            prevtext = "채소 카레 레시피",
            isFavorite = false,
            ingredients = listOf(
                Ingredient("감자", "2개"),
                Ingredient("당근", "1개"),
                Ingredient("양파", "1개"),
                Ingredient("카레 가루", "3큰술"),
                Ingredient("물", "2컵"),
                Ingredient("소금", "약간")
            ),
            contents = listOf(
                RecipeStep(1, "감자, 당근, 양파를 깍둑썰기 하고 냄비에 기름을 두른 후 양파를 볶습니다."),
                RecipeStep(2, "감자와 당근을 넣고 3분간 더 볶은 후 물을 넣어 끓입니다."),
                RecipeStep(3, "감자가 익으면 카레 가루를 넣고 잘 저어줍니다."),
                RecipeStep(4, "약불에서 5분간 더 끓인 후 완성된 카레를 그릇에 담아 맛있게 드세요!"),
            ),
        ),
        RecommendRecipe(
            image = R.drawable.sample_image3,
            title = "비건 떡볶이",
            subtext = "3인분, 30분",
            kcal = "360kcal",
            tag = "락토오보",
            prevtext = "비건 떡볶이 레시피 ",
            isFavorite = false,
            ingredients = listOf(
                Ingredient("떡", "300g"),
                Ingredient("고추장", "2큰술"),
                Ingredient("간장", "1큰술"),
                Ingredient("설탕", "1큰술"),
                Ingredient("양파", "1/2개"),
                Ingredient("양배추", "1/4개"),
                Ingredient("물", "1컵"),
                Ingredient("올리고당", "1큰술")
            ),
            contents = listOf(
                RecipeStep(1, "냄비에 물을 넣고 끓인 후, 고추장, 간장, 설탕을 넣어 양념장을 만듭니다."),
                RecipeStep(2, "양파와 양배추를 썰어 냄비에 넣고 3분간 끓입니다."),
                RecipeStep(3, "떡을 넣고 약불에서 5분간 끓이며 잘 저어줍니다."),
                RecipeStep(4, "올리고당을 추가한 후 떡볶이를 접시에 담아 완성합니다!"),
            ),
        ),
        RecommendRecipe(
            image = R.drawable.sample_image4,
            title = "채식 김밥",
            subtext = "2인분, 20분",
            kcal = "250kcal",
            tag = "페스코",
            prevtext = "채식 김밥 레시피",
            isFavorite = false,
            ingredients = listOf(
                Ingredient("김", "2장"),
                Ingredient("밥", "1공기"),
                Ingredient("당근", "1/2개"),
                Ingredient("오이", "1/2개"),
                Ingredient("단무지", "2줄"),
                Ingredient("참기름", "1큰술"),
                Ingredient("소금", "약간")
            ),
            contents = listOf(
                RecipeStep(1, "당근과 오이를 길게 썰어 준비합니다."),
                RecipeStep(2, "밥에 소금과 참기름을 넣어 간을 맞춥니다."),
                RecipeStep(3, "김 위에 밥을 고르게 펴고 준비한 채소를 올립니다."),
                RecipeStep(4, "김밥을 단단하게 말아 적당한 크기로 자르면 완성!"),
            ),
        ),
        RecommendRecipe(
            image = R.drawable.sample_image5,
            title = "두부 스크램블",
            subtext = "1인분, 15분",
            kcal = "180kcal",
            tag = "비건",
            prevtext = "두부 스크램블 레시피",
            isFavorite = true,
            ingredients = listOf(
                Ingredient("두부", "1모"),
                Ingredient("올리브오일", "1큰술"),
                Ingredient("강황가루", "1작은술"),
                Ingredient("소금", "약간"),
                Ingredient("후추", "약간"),
                Ingredient("양파", "1/4개")
            ),
            contents = listOf(
                RecipeStep(1, "두부를 손으로 부숴 준비합니다."),
                RecipeStep(2, "팬에 올리브오일을 두르고 양파를 볶습니다."),
                RecipeStep(3, "두부를 넣고 강황가루, 소금, 후추를 넣어 볶습니다."),
                RecipeStep(4, "잘 섞어가며 익힌 후 접시에 담아 완성합니다!"),
            ),
        ),
        RecommendRecipe(
            image = R.drawable.sample_image6,
            title = "콩불(콩고기 불고기)",
            subtext = "3인분, 25분",
            kcal = "320kcal",
            tag = "락토오보",
            prevtext = "콩불 레시피",
            isFavorite = false,
            ingredients = listOf(
                Ingredient("콩고기", "200g"),
                Ingredient("양파", "1/2개"),
                Ingredient("대파", "1/2대"),
                Ingredient("간장", "2큰술"),
                Ingredient("설탕", "1큰술"),
                Ingredient("다진 마늘", "1작은술"),
                Ingredient("고춧가루", "1큰술"),
                Ingredient("참기름", "1큰술")
            ),
            contents = listOf(
                RecipeStep(1, "콩고기를 물에 불린 후 물기를 제거합니다."),
                RecipeStep(2, "양파와 대파를 채 썰어 준비합니다."),
                RecipeStep(3, "팬에 간장, 설탕, 다진 마늘, 고춧가루를 넣고 콩고기를 볶습니다."),
                RecipeStep(4, "채소를 추가한 후 참기름을 둘러 완성합니다!"),
            ),
        ),
        RecommendRecipe(
            image = R.drawable.sample_image7,
            title = "비건 마파두부",
            subtext = "2인분, 20분",
            kcal = "290kcal",
            tag = "플렉시테리언",
            prevtext = "매콤한 비건 마파두부 레시피",
            isFavorite = true,
            ingredients = listOf(
                Ingredient("두부", "300g"),
                Ingredient("표고버섯", "50g"),
                Ingredient("양파", "1/2개"),
                Ingredient("대파", "1/2대"),
                Ingredient("고추기름", "1큰술"),
                Ingredient("된장", "1큰술"),
                Ingredient("간장", "1큰술"),
                Ingredient("고춧가루", "1큰술"),
                Ingredient("물", "1/2컵"),
                Ingredient("전분물", "1큰술")
            ),
            contents = listOf(
                RecipeStep(1, "두부는 깍둑썰기하고, 표고버섯과 양파, 대파는 잘게 썰어 준비합니다."),
                RecipeStep(2, "팬에 고추기름을 두르고 양파, 대파, 표고버섯을 볶습니다."),
                RecipeStep(3, "된장, 간장, 고춧가루를 넣고 볶다가 물을 추가합니다."),
                RecipeStep(4, "두부를 넣고 끓인 후 전분물을 넣어 걸쭉하게 만듭니다."),
            ),
        ),
        RecommendRecipe(
            image = R.drawable.sample_image8,
            title = "토마토 두부 스튜",
            subtext = "2인분, 30분",
            kcal = "260kcal",
            tag = "비건",
            prevtext = "건강한 토마토와 두부로 만든 스튜",
            isFavorite = false,
            ingredients = listOf(
                Ingredient("두부", "200g"),
                Ingredient("토마토", "2개"),
                Ingredient("양파", "1/2개"),
                Ingredient("마늘", "2쪽"),
                Ingredient("올리브유", "1큰술"),
                Ingredient("토마토 페이스트", "1큰술"),
                Ingredient("물", "1컵"),
                Ingredient("소금", "1/2작은술"),
                Ingredient("후추", "약간")
            ),
            contents = listOf(
                RecipeStep(1, "두부는 깍둑썰기하고, 토마토와 양파, 마늘을 다집니다."),
                RecipeStep(2, "팬에 올리브유를 두르고 마늘과 양파를 볶습니다."),
                RecipeStep(3, "토마토와 토마토 페이스트를 넣고 중약불에서 끓입니다."),
                RecipeStep(4, "두부를 추가하고 소금, 후추로 간을 맞춘 후 완성합니다."),
            ),
        ),




        )
}
