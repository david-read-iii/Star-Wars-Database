package com.davidread.starwarsdatabase.viewmodel

import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import com.davidread.starwarsdatabase.model.datasource.PageResponse
import com.davidread.starwarsdatabase.model.view.ResourceNameListItem
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Test

/**
 * Unit tests that verify the correctness of [ResourceNamesViewModelImpl]. Using
 * [PersonNamesViewModelImpl] to verify, as it is a subclass of [ResourceNamesViewModelImpl] and a
 * subclass is required.
 */
class ResourceNamesViewModelImplTest : BaseViewModelImplTest() {

    @Test
    fun `given ResourceName with colorControlHighlight, when viewModel calls onFragmentCreateView() with MASTER_DETAIL_SCREEN_WIDTH_DP, then viewModel emits the same ResourceName with colorControlHighlight`() {
        val id = 1
        val response = getSuccessfulPageResponseOfPeople()
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.just(response)
        }
        val viewModel = PersonNamesViewModelImpl(dataSource).apply {
            onResourceNameClick(
                id = id,
                screenWidthDp = BaseViewModelImplTestConstants.MASTER_DETAIL_SCREEN_WIDTH_DP
            )
        }

        val expectedBefore = android.R.attr.colorControlHighlight
        val actualBefore = viewModel.resourceNamesLiveData.value!!
            .filterIsInstance<ResourceNameListItem.ResourceName>()
            .first { it.id == id }.backgroundAttrResId
        Assert.assertEquals(expectedBefore, actualBefore)

        viewModel.onFragmentCreateView(screenWidthDp = BaseViewModelImplTestConstants.MASTER_DETAIL_SCREEN_WIDTH_DP)

        val expectedAfter = android.R.attr.colorControlHighlight
        val actualAfter = viewModel.resourceNamesLiveData.value!!
            .filterIsInstance<ResourceNameListItem.ResourceName>()
            .first { it.id == id }.backgroundAttrResId
        Assert.assertEquals(expectedAfter, actualAfter)
    }

    @Test
    fun `given ResourceName with colorControlHighlight, when viewModel calls onFragmentCreateView() with SINGLE_FRAGMENT_SCREEN_WIDTH_DP, then viewModel emits ResourceNames with selectableItemBackground`() {
        val id = 1
        val response = getSuccessfulPageResponseOfPeople()
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.just(response)
        }
        val viewModel = PersonNamesViewModelImpl(dataSource).apply {
            onResourceNameClick(
                id = id,
                screenWidthDp = BaseViewModelImplTestConstants.MASTER_DETAIL_SCREEN_WIDTH_DP
            )
        }

        val expectedBefore = android.R.attr.colorControlHighlight
        val actualBefore = viewModel.resourceNamesLiveData.value!!
            .filterIsInstance<ResourceNameListItem.ResourceName>()
            .first { it.id == id }.backgroundAttrResId
        Assert.assertEquals(expectedBefore, actualBefore)

        viewModel.onFragmentCreateView(screenWidthDp = BaseViewModelImplTestConstants.SINGLE_FRAGMENT_SCREEN_WIDTH_DP)

        val expectedAfter = android.R.attr.selectableItemBackground
        val actualAfter = viewModel.resourceNamesLiveData.value!!
            .filterIsInstance<ResourceNameListItem.ResourceName>()
            .first { it.id == id }.backgroundAttrResId
        Assert.assertEquals(expectedAfter, actualAfter)
    }

    @Test
    fun `when viewModel calls onResourceNameClick() with SINGLE_FRAGMENT_SCREEN_WIDTH_DP, then viewModel does not emit a ResourceName with colorControlHighlight`() {
        val id = 1
        val response = getSuccessfulPageResponseOfPeople()
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.just(response)
        }
        val viewModel = PersonNamesViewModelImpl(dataSource).apply {
            onResourceNameClick(
                id = id,
                screenWidthDp = BaseViewModelImplTestConstants.SINGLE_FRAGMENT_SCREEN_WIDTH_DP
            )
        }

        val expected = android.R.attr.selectableItemBackground
        val actual = viewModel.resourceNamesLiveData.value!!
            .filterIsInstance<ResourceNameListItem.ResourceName>()
            .first { it.id == id }.backgroundAttrResId
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when viewModel calls onResourceNameClick() with MASTER_DETAIL_SCREEN_WIDTH_DP, then viewModel does emits the same ResourceName with colorControlHighlight`() {
        val id = 1
        val response = getSuccessfulPageResponseOfPeople()
        val dataSource = mockk<PeopleRemoteDataSource> {
            every { getPeople(any()) } returns Single.just(response)
        }
        val viewModel = PersonNamesViewModelImpl(dataSource).apply {
            onResourceNameClick(
                id = id,
                screenWidthDp = BaseViewModelImplTestConstants.MASTER_DETAIL_SCREEN_WIDTH_DP
            )
        }

        val expected = android.R.attr.colorControlHighlight
        val actual = viewModel.resourceNamesLiveData.value!!
            .filterIsInstance<ResourceNameListItem.ResourceName>()
            .first { it.id == id }.backgroundAttrResId
        Assert.assertEquals(expected, actual)
    }

    private fun getSuccessfulPageResponseOfPeople(next: String? = null) = PageResponse(
        results = IntRange(1, 10).map { id ->
            BaseViewModelImplTestConstants.getSuccessfulPersonResponse(id)
        },
        next = next
    )
}