/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.in.kafka.avro.cart;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class CartData extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -1387127706340020191L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"CartData\",\"namespace\":\"com.in.kafka.avro.cart\",\"fields\":[{\"name\":\"cartProducts\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"CartProduct\",\"fields\":[{\"name\":\"productId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"quantity\",\"type\":\"int\"}]}}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<CartData> ENCODER =
      new BinaryMessageEncoder<CartData>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<CartData> DECODER =
      new BinaryMessageDecoder<CartData>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<CartData> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<CartData> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<CartData> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<CartData>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this CartData to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a CartData from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a CartData instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static CartData fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private java.util.List<com.in.kafka.avro.cart.CartProduct> cartProducts;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public CartData() {}

  /**
   * All-args constructor.
   * @param cartProducts The new value for cartProducts
   */
  public CartData(java.util.List<com.in.kafka.avro.cart.CartProduct> cartProducts) {
    this.cartProducts = cartProducts;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return cartProducts;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: cartProducts = (java.util.List<com.in.kafka.avro.cart.CartProduct>)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'cartProducts' field.
   * @return The value of the 'cartProducts' field.
   */
  public java.util.List<com.in.kafka.avro.cart.CartProduct> getCartProducts() {
    return cartProducts;
  }


  /**
   * Sets the value of the 'cartProducts' field.
   * @param value the value to set.
   */
  public void setCartProducts(java.util.List<com.in.kafka.avro.cart.CartProduct> value) {
    this.cartProducts = value;
  }

  /**
   * Creates a new CartData RecordBuilder.
   * @return A new CartData RecordBuilder
   */
  public static com.in.kafka.avro.cart.CartData.Builder newBuilder() {
    return new com.in.kafka.avro.cart.CartData.Builder();
  }

  /**
   * Creates a new CartData RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new CartData RecordBuilder
   */
  public static com.in.kafka.avro.cart.CartData.Builder newBuilder(com.in.kafka.avro.cart.CartData.Builder other) {
    if (other == null) {
      return new com.in.kafka.avro.cart.CartData.Builder();
    } else {
      return new com.in.kafka.avro.cart.CartData.Builder(other);
    }
  }

  /**
   * Creates a new CartData RecordBuilder by copying an existing CartData instance.
   * @param other The existing instance to copy.
   * @return A new CartData RecordBuilder
   */
  public static com.in.kafka.avro.cart.CartData.Builder newBuilder(com.in.kafka.avro.cart.CartData other) {
    if (other == null) {
      return new com.in.kafka.avro.cart.CartData.Builder();
    } else {
      return new com.in.kafka.avro.cart.CartData.Builder(other);
    }
  }

  /**
   * RecordBuilder for CartData instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<CartData>
    implements org.apache.avro.data.RecordBuilder<CartData> {

    private java.util.List<com.in.kafka.avro.cart.CartProduct> cartProducts;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.in.kafka.avro.cart.CartData.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.cartProducts)) {
        this.cartProducts = data().deepCopy(fields()[0].schema(), other.cartProducts);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
    }

    /**
     * Creates a Builder by copying an existing CartData instance
     * @param other The existing instance to copy.
     */
    private Builder(com.in.kafka.avro.cart.CartData other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.cartProducts)) {
        this.cartProducts = data().deepCopy(fields()[0].schema(), other.cartProducts);
        fieldSetFlags()[0] = true;
      }
    }

    /**
      * Gets the value of the 'cartProducts' field.
      * @return The value.
      */
    public java.util.List<com.in.kafka.avro.cart.CartProduct> getCartProducts() {
      return cartProducts;
    }


    /**
      * Sets the value of the 'cartProducts' field.
      * @param value The value of 'cartProducts'.
      * @return This builder.
      */
    public com.in.kafka.avro.cart.CartData.Builder setCartProducts(java.util.List<com.in.kafka.avro.cart.CartProduct> value) {
      validate(fields()[0], value);
      this.cartProducts = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'cartProducts' field has been set.
      * @return True if the 'cartProducts' field has been set, false otherwise.
      */
    public boolean hasCartProducts() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'cartProducts' field.
      * @return This builder.
      */
    public com.in.kafka.avro.cart.CartData.Builder clearCartProducts() {
      cartProducts = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CartData build() {
      try {
        CartData record = new CartData();
        record.cartProducts = fieldSetFlags()[0] ? this.cartProducts : (java.util.List<com.in.kafka.avro.cart.CartProduct>) defaultValue(fields()[0]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<CartData>
    WRITER$ = (org.apache.avro.io.DatumWriter<CartData>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<CartData>
    READER$ = (org.apache.avro.io.DatumReader<CartData>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    long size0 = this.cartProducts.size();
    out.writeArrayStart();
    out.setItemCount(size0);
    long actualSize0 = 0;
    for (com.in.kafka.avro.cart.CartProduct e0: this.cartProducts) {
      actualSize0++;
      out.startItem();
      e0.customEncode(out);
    }
    out.writeArrayEnd();
    if (actualSize0 != size0)
      throw new java.util.ConcurrentModificationException("Array-size written was " + size0 + ", but element count was " + actualSize0 + ".");

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      long size0 = in.readArrayStart();
      java.util.List<com.in.kafka.avro.cart.CartProduct> a0 = this.cartProducts;
      if (a0 == null) {
        a0 = new SpecificData.Array<com.in.kafka.avro.cart.CartProduct>((int)size0, SCHEMA$.getField("cartProducts").schema());
        this.cartProducts = a0;
      } else a0.clear();
      SpecificData.Array<com.in.kafka.avro.cart.CartProduct> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<com.in.kafka.avro.cart.CartProduct>)a0 : null);
      for ( ; 0 < size0; size0 = in.arrayNext()) {
        for ( ; size0 != 0; size0--) {
          com.in.kafka.avro.cart.CartProduct e0 = (ga0 != null ? ga0.peek() : null);
          if (e0 == null) {
            e0 = new com.in.kafka.avro.cart.CartProduct();
          }
          e0.customDecode(in);
          a0.add(e0);
        }
      }

    } else {
      for (int i = 0; i < 1; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          long size0 = in.readArrayStart();
          java.util.List<com.in.kafka.avro.cart.CartProduct> a0 = this.cartProducts;
          if (a0 == null) {
            a0 = new SpecificData.Array<com.in.kafka.avro.cart.CartProduct>((int)size0, SCHEMA$.getField("cartProducts").schema());
            this.cartProducts = a0;
          } else a0.clear();
          SpecificData.Array<com.in.kafka.avro.cart.CartProduct> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<com.in.kafka.avro.cart.CartProduct>)a0 : null);
          for ( ; 0 < size0; size0 = in.arrayNext()) {
            for ( ; size0 != 0; size0--) {
              com.in.kafka.avro.cart.CartProduct e0 = (ga0 != null ? ga0.peek() : null);
              if (e0 == null) {
                e0 = new com.in.kafka.avro.cart.CartProduct();
              }
              e0.customDecode(in);
              a0.add(e0);
            }
          }
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










