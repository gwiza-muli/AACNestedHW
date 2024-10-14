package edu.grinnell.csc207.util;
import static java.lang.reflect.Array.newInstance;

/**
 * A basic implementation of Associative Arrays with keys of type K and values of type V.
 * Associative Arrays store key/value pairs and permit you to look up values by key.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Sheilla Muligande
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V>[] pairs;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({"unchecked"})
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(), DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   *
   * @return a new copy of the array
   */
  @SuppressWarnings({"unchecked"})
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> copyArray = new AssociativeArray<>();
    copyArray.size = this.size;
    copyArray.pairs = new KVPair[this.pairs.length];
    for (int i = 0; i < this.pairs.length; i++) {
      if (this.pairs[i] != null) {
        copyArray.pairs[i] = this.pairs[i].clone();
      } // if
    } // for
    return copyArray;
  } // clone()

  /**
   * Convert the array to a string.
   *
   * @return a string of the form "{Key0:Value0, Key1:Value1, ... KeyN:ValueN}"
   */
  public String toString() {
    if (size == 0) {
      return "{}";
    }

    String arrayString = "{";
    int numElements = 0;

    for (int i = 0; i < pairs.length; i++) {
      if (pairs[i] != null) {
        if (numElements > 0) {
          arrayString += ", ";
        } // if
        arrayString += pairs[i].key + ":" + pairs[i].val;
        numElements++;
      } // if
    } // for

    return (arrayString + "}");
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to get(key) will return value.
   *
   * @param key The key whose value we are seeting.
   * @param value The value of that key.
   *
   * @throws NullKeyException If the client provides a null key.
   */
  public void set(K key, V value) throws NullKeyException {
    if (key == null) {
      throw new NullKeyException();
    } // if

    try {
      int keyIndex = find(key);
      pairs[keyIndex].val = value;
      return;
    } catch (KeyNotFoundException e) {

    } // try/catch

    if (size >= pairs.length) {
      expand();
    }

    for (int i = 0; i < pairs.length; i++) {
      if (pairs[i] == null) {
        pairs[i] = new KVPair<>(key, value);
        size++;
        return;
      } // if
    } // for

    // expand();
    // pairs[size] = new KVPair<>(key, value);
    // size++;

  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @param key A key
   *
   * @return The corresponding value
   *
   * @throws KeyNotFoundException when the key is null or does not appear in the associative array.
   */
  public V get(K key) throws KeyNotFoundException {
    int keyIndex = find(key);
    return pairs[keyIndex].val;
  } // get(K)

  /**
   * Determine if key appears in the associative array. Should return false for the null key, since
   * it cannot appear.
   *
   * @param key The key we're looking for.
   *
   * @return true if the key appears and false otherwise.
   */
  public boolean hasKey(K key) {
    
    try {
      if (key == null) {
        return false;
      } // if
      find(key);
      return true;
    } catch (KeyNotFoundException e) {
      return false;
    } // try/catch

  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls to get(key) will throw an
   * exception. If the key does not appear in the associative array, does nothing.
   *
   * @param key The key to remove.
   */
  public void remove(K key) {

    int keyIndex;
    try {
      keyIndex = find(key);
      pairs[keyIndex] = null;
      size--;
    } catch (KeyNotFoundException e) {
      return;
    } // try/catch
  } // remove(K)

  /**
   * Determine how many key/value pairs are in the associative array.
   *
   * @return The number of key/value pairs in the array.
   */
  public int size() {
    return this.size;
  } // size()

  @SuppressWarnings("unchecked")
  
  public String[] getKeyArray() {
    if (this.size == 0) {
      return new String[0];
    }

    int keysLength = 0;
    for (int i = 0; i < this.pairs.length; i++) {
      if (pairs[i] != null) {
        keysLength++;
      }
    }

    String[] keysArr = new String[keysLength];

     int keyIndex = 0;
    for (int x = 0; x < this.pairs.length; x++) {
      if (pairs[x] != null) {
        keysArr[keyIndex++] = (String) pairs[x].key;
      }
    }

    return keysArr;
  }

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key. If no such entry is found,
   * throws an exception.
   *
   * @param key The key of the entry.
   *
   * @return The index of the key, if found.
   *
   * @throws KeyNotFoundException If the key does not appear in the associative array.
   */
  int find(K key) throws KeyNotFoundException {
    if (key == null) {
      throw new KeyNotFoundException();
    } // if
    for (int k = 0; k < pairs.length; k++) {
      if (pairs[k] != null && pairs[k].key != null && pairs[k].key.equals(key)) {
        return k;
      } // if
    } // for

    throw new KeyNotFoundException();
  } // find(K)

} // class AssociativeArray
