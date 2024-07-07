//package ru.domain.managers;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import ru.domain.entities.ConferenceRoom;
//
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ConferenceRoomFilterTest {
//
//    private ConferenceRoomFilter conferenceRoomFilter;
//    private Map<String, ConferenceRoom> conferenceRooms;
//
//    @BeforeEach
//    public void setUp() {
//        conferenceRooms = new HashMap<>();
//        conferenceRoomFilter = new ConferenceRoomFilter(conferenceRooms);
//    }
//
//    @Test
//    public void testFilterByDate() {
//        LocalDate date = LocalDate.of(2024, 7, 7);
//        ConferenceRoom room1 = mock(ConferenceRoom.class);
//        ConferenceRoom room2 = mock(ConferenceRoom.class);
//
//        when(room1.hasBookingOnDate(date)).thenReturn(true);
//        when(room1.getName()).thenReturn("Room 1");
//        when(room2.hasBookingOnDate(date)).thenReturn(false);
//
//        conferenceRooms.put("Room 1", room1);
//        conferenceRooms.put("Room 2", room2);
//
//        List<String> result = conferenceRoomFilter.filterByDate(date);
//
//        assertThat(result).containsExactly("Room 1 has booking on 2024-07-07");
//        verify(room1, times(1)).hasBookingOnDate(date);
//        verify(room2, times(1)).hasBookingOnDate(date);
//    }
//
//    @Test
//    public void testFilterByUser() {
//        String userName = "user1";
//        ConferenceRoom room1 = mock(ConferenceRoom.class);
//        ConferenceRoom room2 = mock(ConferenceRoom.class);
//
//        when(room1.hasBookingByUser(userName)).thenReturn(true);
//        when(room1.getName()).thenReturn("Room 1");
//        when(room2.hasBookingByUser(userName)).thenReturn(false);
//
//        conferenceRooms.put("Room 1", room1);
//        conferenceRooms.put("Room 2", room2);
//
//        List<String> result = conferenceRoomFilter.filterByUser(userName);
//
//        assertThat(result).containsExactly("Room 1 has booking by user user1");
//        verify(room1, times(1)).hasBookingByUser(userName);
//        verify(room2, times(1)).hasBookingByUser(userName);
//    }
//
//    @Test
//    public void testFilterByAvailableWorkspaces() {
//        ConferenceRoom room1 = mock(ConferenceRoom.class);
//        ConferenceRoom room2 = mock(ConferenceRoom.class);
//
//        when(room1.hasAvailableWorkspaces()).thenReturn(true);
//        when(room1.getName()).thenReturn("Room 1");
//        when(room2.hasAvailableWorkspaces()).thenReturn(false);
//
//        conferenceRooms.put("Room 1", room1);
//        conferenceRooms.put("Room 2", room2);
//
//        List<String> result = conferenceRoomFilter.filterByAvailableWorkspaces();
//
//        assertThat(result).containsExactly("Room 1 has available workspaces");
//        verify(room1, times(1)).hasAvailableWorkspaces();
//        verify(room2, times(1)).hasAvailableWorkspaces();
//    }
//}