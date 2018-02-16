package net.consensys.tools.ipfs.ipfsstore.test.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable.ByteArrayWrapper;
import io.ipfs.multihash.Multihash;
import net.consensys.tools.ipfs.ipfsstore.dao.StorageDao;
import net.consensys.tools.ipfs.ipfsstore.dao.impl.IPFSStorageDao;
import net.consensys.tools.ipfs.ipfsstore.exception.DaoException;

@RunWith(SpringJUnit4ClassRunner.class)
public class IPFSStorageDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPFSStorageDAOTest.class);
    
    private StorageDao underTest;
    
    @MockBean
    private IPFS ipfs;
    
    @Before
    public void setup() {
        underTest = new IPFSStorageDao(ipfs);
    }
    
    
    // #########################################################
    // ####################### createContent
    // #########################################################
    
    @Test
    public void createContentTest() throws IOException, DaoException {
        String content = "{\"hello\": \"world\"}";
        String hash = "QmNN4RaVXNMVaEPLrmS7SUQpPZEQ2eJ6s5WxLw9w4GTm34";
        
        // Mock
        List<MerkleNode> merklenodes = new ArrayList<MerkleNode>();
        MerkleNode merklenode = new MerkleNode(hash);
        merklenodes.add(merklenode);
        
        Mockito.when(ipfs.add(any(ByteArrayWrapper.class))).thenReturn(merklenodes);
        
        // #################################################
        String hashResult = underTest.createContent(content.getBytes());
        // #################################################
        
        LOGGER.debug("hashResult="+hashResult);
        
        assertEquals("Hash should be " + hash, hash, hashResult);
        
        Mockito.verify(ipfs, Mockito.times(1)).add(any(ByteArrayWrapper.class));  
    }
    
    @Test(expected=DaoException.class)
    public void createContentTestException() throws IOException, DaoException {
        String content = "{\"hello\": \"world\"}";
        
        // Mock
        Mockito.when(ipfs.add(any(ByteArrayWrapper.class))).thenThrow(new IOException(""));
        
        // #################################################
        underTest.createContent(content.getBytes());
        // ################################################# 
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void createNullContentTestException() throws IOException, DaoException {
        
        // #################################################
        underTest.createContent(null);
        // ################################################# 
    }
    

    
    // #########################################################
    // ####################### getContent
    // #########################################################
    
    @Test
    public void getContentTest() throws IOException, DaoException {
        String content = "{\"hello\": \"world\"}";
        String hash = "QmNN4RaVXNMVaEPLrmS7SUQpPZEQ2eJ6s5WxLw9w4GTm34";
        
        // Mock
        Mockito.when(ipfs.cat(any(Multihash.class))).thenReturn(content.getBytes());
        
        // #################################################
        byte[] contentResult = underTest.getContent(hash);
        // #################################################
        
        LOGGER.debug("contentResult="+new String(contentResult));
        
        assertEquals("Content should be " + content, content, new String(contentResult));
        
        Mockito.verify(ipfs, Mockito.times(1)).cat(any(Multihash.class));  
    }

    @Test(expected=DaoException.class)
    public void getContentTestException() throws IOException, DaoException {
        String hash = "QmNN4RaVXNMVaEPLrmS7SUQpPZEQ2eJ6s5WxLw9w4GTm34";
        
        // Mock
        Mockito.when(ipfs.cat(any(Multihash.class))).thenThrow(new IOException(""));
        
        // #################################################
        underTest.getContent(hash);
        // ################################################# 
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getContentTestIllegalArgumentException() throws IOException, DaoException {
        // #################################################
        underTest.getContent("");
        // ################################################# 
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getContentTestIllegalArgumentException2() throws IOException, DaoException {
        // #################################################
        underTest.getContent(null);
        // ################################################# 
    }
    
    
//    // #########################################################
//    // ####################### pin
//    // #########################################################
//    
//    @Test
//    public void pinTestOK() throws IOException, DaoException {
//        String hash = "QmNN4RaVXNMVaEPLrmS7SUQpPZEQ2eJ6s5WxLw9w4GTm34";
//        
//        // Mock
//        Mockito.when(pin.add(any(Multihash.class))).thenReturn(new ArrayList<>());
//        
//        // #################################################
//        underTest.pin(hash);
//        // #################################################
//        
//        Mockito.verify(ipfs.pin, Mockito.times(1)).add(any(Multihash.class));  
//    }
//    
//    @Test(expected=DaoException.class)
//    public void pinTestException() throws IOException, DaoException {
//        String hash = "QmNN4RaVXNMVaEPLrmS7SUQpPZEQ2eJ6s5WxLw9w4GTm34";
//        
//        // Mock
//        Mockito.when(ipfs.pin.add(any(Multihash.class))).thenThrow(new IOException(""));
//        
//        // #################################################
//        underTest.pin(hash);
//        // ################################################# 
//    }
//    
//    @Test(expected=IllegalArgumentException.class)
//    public void pinTestIllegalArgumentException() throws IOException, DaoException {
//        // #################################################
//        underTest.pin("");
//        // ################################################# 
//    }
//    
//    
//    // #########################################################
//    // ####################### unpin
//    // #########################################################
//    
//    @Test
//    public void unpinTestOK() throws IOException, DaoException {
//        String hash = "QmNN4RaVXNMVaEPLrmS7SUQpPZEQ2eJ6s5WxLw9w4GTm34";
//        
//        // Mock
//        Mockito.when(ipfs.pin.rm(any(Multihash.class))).thenReturn(new ArrayList<>());
//        
//        // #################################################
//        underTest.unpin(hash);
//        // #################################################
//        
//        Mockito.verify(ipfs, Mockito.times(1)).pin.rm(any(Multihash.class));  
//    }
//    
//    @Test(expected=DaoException.class)
//    public void unpinTestException() throws IOException, DaoException {
//        String hash = "QmNN4RaVXNMVaEPLrmS7SUQpPZEQ2eJ6s5WxLw9w4GTm34";
//        
//        // Mock
//        Mockito.when(ipfs.pin.rm(any(Multihash.class))).thenThrow(new IOException(""));
//        
//        // #################################################
//        underTest.unpin(hash);
//        // ################################################# 
//    }
//    
//    @Test(expected=IllegalArgumentException.class)
//    public void unpinTestIllegalArgumentException() throws IOException, DaoException {
//        // #################################################
//        underTest.unpin("");
//        // ################################################# 
//    }
    
    
}
