/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.in.kafka.avro.product;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class ProductViewCount extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -4253765097858330808L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ProductViewCount\",\"namespace\":\"com.in.kafka.avro.product\",\"fields\":[{\"name\":\"categoryId\",\"type\":\"string\"},{\"name\":\"viewCount\",\"type\":\"int\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<ProductViewCount> ENCODER =
      new BinaryMessageEncoder<ProductViewCount>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<ProductViewCount> DECODER =
      new BinaryMessageDecoder<ProductViewCount>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<ProductViewCount> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<ProductViewCount> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<ProductViewCount> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<ProductViewCount>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this ProductViewCount to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a ProductViewCount from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a ProductViewCount instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static ProductViewCount fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private java.lang.CharSequence categoryId;
  private int viewCount;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public ProductViewCount() {}

  /**
   * All-args constructor.
   * @param categoryId The new value for categoryId
   * @param viewCount The new value for viewCount
   */
  public ProductViewCount(java.lang.CharSequence categoryId, java.lang.Integer viewCount) {
    this.categoryId = categoryId;
    this.viewCount = viewCount;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return categoryId;
    case 1: return viewCount;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: categoryId = (java.lang.CharSequence)value$; break;
    case 1: viewCount = (java.lang.Integer)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'categoryId' field.
   * @return The value of the 'categoryId' field.
   */
  public java.lang.CharSequence getCategoryId() {
    return categoryId;
  }


  /**
   * Sets the value of the 'categoryId' field.
   * @param value the value to set.
   */
  public void setCategoryId(java.lang.CharSequence value) {
    this.categoryId = value;
  }

  /**
   * Gets the value of the 'viewCount' field.
   * @return The value of the 'viewCount' field.
   */
  public int getViewCount() {
    return viewCount;
  }


  /**
   * Sets the value of the 'viewCount' field.
   * @param value the value to set.
   */
  public void setViewCount(int value) {
    this.viewCount = value;
  }

  /**
   * Creates a new ProductViewCount RecordBuilder.
   * @return A new ProductViewCount RecordBuilder
   */
  public static com.in.kafka.avro.product.ProductViewCount.Builder newBuilder() {
    return new com.in.kafka.avro.product.ProductViewCount.Builder();
  }

  /**
   * Creates a new ProductViewCount RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new ProductViewCount RecordBuilder
   */
  public static com.in.kafka.avro.product.ProductViewCount.Builder newBuilder(com.in.kafka.avro.product.ProductViewCount.Builder other) {
    if (other == null) {
      return new com.in.kafka.avro.product.ProductViewCount.Builder();
    } else {
      return new com.in.kafka.avro.product.ProductViewCount.Builder(other);
    }
  }

  /**
   * Creates a new ProductViewCount RecordBuilder by copying an existing ProductViewCount instance.
   * @param other The existing instance to copy.
   * @return A new ProductViewCount RecordBuilder
   */
  public static com.in.kafka.avro.product.ProductViewCount.Builder newBuilder(com.in.kafka.avro.product.ProductViewCount other) {
    if (other == null) {
      return new com.in.kafka.avro.product.ProductViewCount.Builder();
    } else {
      return new com.in.kafka.avro.product.ProductViewCount.Builder(other);
    }
  }

  /**
   * RecordBuilder for ProductViewCount instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<ProductViewCount>
    implements org.apache.avro.data.RecordBuilder<ProductViewCount> {

    private java.lang.CharSequence categoryId;
    private int viewCount;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.in.kafka.avro.product.ProductViewCount.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.categoryId)) {
        this.categoryId = data().deepCopy(fields()[0].schema(), other.categoryId);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.viewCount)) {
        this.viewCount = data().deepCopy(fields()[1].schema(), other.viewCount);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
    }

    /**
     * Creates a Builder by copying an existing ProductViewCount instance
     * @param other The existing instance to copy.
     */
    private Builder(com.in.kafka.avro.product.ProductViewCount other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.categoryId)) {
        this.categoryId = data().deepCopy(fields()[0].schema(), other.categoryId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.viewCount)) {
        this.viewCount = data().deepCopy(fields()[1].schema(), other.viewCount);
        fieldSetFlags()[1] = true;
      }
    }

    /**
      * Gets the value of the 'categoryId' field.
      * @return The value.
      */
    public java.lang.CharSequence getCategoryId() {
      return categoryId;
    }


    /**
      * Sets the value of the 'categoryId' field.
      * @param value The value of 'categoryId'.
      * @return This builder.
      */
    public com.in.kafka.avro.product.ProductViewCount.Builder setCategoryId(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.categoryId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'categoryId' field has been set.
      * @return True if the 'categoryId' field has been set, false otherwise.
      */
    public boolean hasCategoryId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'categoryId' field.
      * @return This builder.
      */
    public com.in.kafka.avro.product.ProductViewCount.Builder clearCategoryId() {
      categoryId = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'viewCount' field.
      * @return The value.
      */
    public int getViewCount() {
      return viewCount;
    }


    /**
      * Sets the value of the 'viewCount' field.
      * @param value The value of 'viewCount'.
      * @return This builder.
      */
    public com.in.kafka.avro.product.ProductViewCount.Builder setViewCount(int value) {
      validate(fields()[1], value);
      this.viewCount = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'viewCount' field has been set.
      * @return True if the 'viewCount' field has been set, false otherwise.
      */
    public boolean hasViewCount() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'viewCount' field.
      * @return This builder.
      */
    public com.in.kafka.avro.product.ProductViewCount.Builder clearViewCount() {
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ProductViewCount build() {
      try {
        ProductViewCount record = new ProductViewCount();
        record.categoryId = fieldSetFlags()[0] ? this.categoryId : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.viewCount = fieldSetFlags()[1] ? this.viewCount : (java.lang.Integer) defaultValue(fields()[1]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<ProductViewCount>
    WRITER$ = (org.apache.avro.io.DatumWriter<ProductViewCount>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<ProductViewCount>
    READER$ = (org.apache.avro.io.DatumReader<ProductViewCount>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.categoryId);

    out.writeInt(this.viewCount);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.categoryId = in.readString(this.categoryId instanceof Utf8 ? (Utf8)this.categoryId : null);

      this.viewCount = in.readInt();

    } else {
      for (int i = 0; i < 2; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.categoryId = in.readString(this.categoryId instanceof Utf8 ? (Utf8)this.categoryId : null);
          break;

        case 1:
          this.viewCount = in.readInt();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










